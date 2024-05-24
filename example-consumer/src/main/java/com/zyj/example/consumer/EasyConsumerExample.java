package com.zyj.example.consumer;

import com.zyj.example.common.model.User;
import com.zyj.example.common.service.UserService;
import com.zyj.zyjrpc.proxy.ServiceProxyFactory;

/**
 * 简易服务消费者示例
 *
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        // 动态代理 , 获取服务对应的代理对象
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
        short number = userService.getNumber();
        System.out.println(number);
    }
}