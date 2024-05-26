package com.zyj.zyjrpc;

import com.zyj.zyjrpc.config.RegistryConfig;
import com.zyj.zyjrpc.config.RpcConfig;
import com.zyj.zyjrpc.constant.RpcConstant;
import com.zyj.zyjrpc.registry.Registry;
import com.zyj.zyjrpc.registry.RegistryFactory;
import com.zyj.zyjrpc.utils.ConfigUtils;

public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);

        // 创建并注册一个 shutdown Hook ， JVM退出时执行操作 --> 配合实现主动服务下线功能
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destory));
    }

    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class , RpcConstant.DEFAULT_CONFIG_PRCFIX);
        } catch (Exception e) {
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig() {
        if(rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if(rpcConfig == null) {
                    init();
                }
            }
        }
        return RpcApplication.rpcConfig;
    }
}