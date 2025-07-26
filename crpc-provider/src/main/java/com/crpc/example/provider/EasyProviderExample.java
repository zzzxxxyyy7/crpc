package com.crpc.example.provider;

import com.crpc.example.common.service.UserService;
import com.crpc.registry.LocalRegistry;
import com.crpc.server.Server;
import com.crpc.server.http.VertxHttpServer;

/**
 * 简易服务提供者示例
 *

 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        Server server = new VertxHttpServer();
        server.doStart(8080);
    }
}
