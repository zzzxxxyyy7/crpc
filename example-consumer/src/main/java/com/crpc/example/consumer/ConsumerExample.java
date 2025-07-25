package com.crpc.example.consumer;

import com.crpc.example.common.model.User;
import com.crpc.example.common.service.UserService;
import com.crpc.bootstrap.ConsumerBootstrap;
import com.crpc.proxy.ServiceProxyFactory;

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
