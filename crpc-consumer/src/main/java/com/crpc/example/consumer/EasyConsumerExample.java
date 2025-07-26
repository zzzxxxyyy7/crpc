package com.crpc.example.consumer;

import com.crpc.example.common.model.User;
import com.crpc.example.common.service.UserService;
import com.crpc.proxy.ServiceProxyFactory;

/**
 * 简易服务消费者示例
 *

 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        // 动态代理
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
