package com.zyj.example.provider;

import com.zyj.example.common.service.UserService;
import com.zyj.zyjrpc.RpcApplication;
import com.zyj.zyjrpc.config.RegistryConfig;
import com.zyj.zyjrpc.config.RpcConfig;
import com.zyj.zyjrpc.model.ServiceMetaInfo;
import com.zyj.zyjrpc.registry.LocalRegistry;
import com.zyj.zyjrpc.registry.Registry;
import com.zyj.zyjrpc.registry.RegistryFactory;
import com.zyj.zyjrpc.server.Server;
import com.zyj.zyjrpc.server.TCP.VertxTcpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 HTTP web 服务
//        Server httpServer = new VertxHttpServer();
//        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

//      启动 TCP web 服务
        Server tcpServer = new VertxTcpServer();
        tcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}