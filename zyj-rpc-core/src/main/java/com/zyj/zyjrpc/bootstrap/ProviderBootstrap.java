package com.zyj.zyjrpc.bootstrap;

import com.zyj.zyjrpc.RpcApplication;
import com.zyj.zyjrpc.config.RegistryConfig;
import com.zyj.zyjrpc.config.RpcConfig;
import com.zyj.zyjrpc.model.ServiceMetaInfo;
import com.zyj.zyjrpc.model.ServiceRegisterInfo;
import com.zyj.zyjrpc.registry.LocalRegistry;
import com.zyj.zyjrpc.registry.Registry;
import com.zyj.zyjrpc.registry.RegistryFactory;
import com.zyj.zyjrpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * 服务提供者启动类（初始化）

 */
public class ProviderBootstrap {

    /**
     * 初始化
     */
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();
        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            // 本地注册
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());

            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }

        // 启动服务器
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());
    }
}
