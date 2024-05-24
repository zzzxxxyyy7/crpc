package com.zyj.example.provider;

import com.zyj.example.common.service.UserService;
import com.zyj.zyjrpc.RpcApplication;
import com.zyj.zyjrpc.registry.LocalRegistry;
import com.zyj.zyjrpc.server.HttpServer;
import com.zyj.zyjrpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 *
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}