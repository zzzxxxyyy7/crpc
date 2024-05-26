package com.zyj.zyjrpc.registry.ETCD;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.zyj.zyjrpc.config.RegistryConfig;
import com.zyj.zyjrpc.model.ServiceMetaInfo;
import com.zyj.zyjrpc.registry.RegisterServiceCache;
import com.zyj.zyjrpc.registry.Registry;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Etcd 注册中心
 *
 */
public class EtcdRegistry implements Registry {
    private Client client;

    private KV kvClient;

    /**
     * 本地注册节点的 key 集合
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    /**
     * 注册中心服务缓存
     */
    private final RegisterServiceCache registerServiceCache = new RegisterServiceCache();

    /**
     * 正在监听的 Key 集合
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 创建 Lease 和 KV 客户端
        Lease leaseClient = client.getLeaseClient();

        // 创建一个 30 秒的租约
        long leaseId = leaseClient.grant(30).get().getID();

        // 设置要存储的键值对
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 将键值对与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();

        // 添加到本地注册节点的 key 集合 , 本地缓存
        localRegisterNodeKeySet.add(registerKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registerKey, StandardCharsets.UTF_8));
        // 删除本地注册节点的 key 集合
        localRegisterNodeKeySet.remove(registerKey);
    }

    @Override
    public void watch(String serviceNodeKey) {
        // 初始化监听器
        Watch watchClient = client.getWatchClient();
        // 开启对键的监听 , 避免重复
        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if(newWatch) {
            watchClient.watch(
                    ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8) , Response->{
                        for(WatchEvent event : Response.getEvents()) {
                            switch (event.getEventType()) {
                                case DELETE:
                                    registerServiceCache.clearCache();
                                case PUT:
                                default:
                                    break;
                            }
                        }
                    }
            );
        }
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // Fix 先从服务缓存中获取服务
        List<ServiceMetaInfo> cachedServiceMetaInfoList = registerServiceCache.readCache();
        if(cachedServiceMetaInfoList != null) {
            return cachedServiceMetaInfoList;
        }

        // 前缀搜索，结尾一定要加 '/'
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

        try {
            // 前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                            ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();
            // 解析服务信息
            List<ServiceMetaInfo> serviceMetaInfoList = keyValues.stream().map(keyValue -> {
                String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                // 监听 key 的变化 , 当服务注册信息发生变更的时候，及时更新消费端服务缓存
                watch(key);
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());

            // 写入服务缓存
            registerServiceCache.writCache(serviceMetaInfoList);
            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败", e);
        }
    }

    /**
     * 心跳监听机制
     */
    @Override
    public void heartBeat() {
        // 10 秒续签一次
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                System.out.println("心跳监听执行中: " + LocalDateTime.now());
                // 遍历本节点所有的 key
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> keyValues = kvClient
                                .get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        // 该节点已过期（需要重启节点才能重新注册）
                        if (CollUtil.isEmpty(keyValues)) {
                            continue;
                        }
                        // 节点未过期，重新注册（相当于续签）
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(key + "续签失败", e);
                    }
                }
            }
        });

        // 支持秒级别任务
        CronUtil.setMatchSecond(true);
        // 启动定时任务
        CronUtil.start();
    }

    @Override
    public void destory(){
        System.out.println("当前服务节点下线");

        // 下线节点
        // 遍历本节点所以的 key
        for(String key : localRegisterNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(key + "下线失败", e);
            }
        }

        // 下线节点
        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }
}