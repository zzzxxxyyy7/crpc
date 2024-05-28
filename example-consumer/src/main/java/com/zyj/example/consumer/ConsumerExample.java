package com.zyj.example.consumer;

import com.zyj.example.common.model.User;
import com.zyj.example.common.service.UserService;
import com.zyj.zyjrpc.bootstrap.ConsumerBootstrap;
import com.zyj.zyjrpc.proxy.ServiceProxyFactory;

/**
 * 服务消费者示例
 *

 */
public class ConsumerExample {

    public static void main(String[] args) {
        // 服务提供者初始化
        ConsumerBootstrap.init();

        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("zyj");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
