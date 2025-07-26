package com.crpc.springbootconsumer.service.impl;

import com.crpc.crpc.springboot.starter.annotation.RpcAutoworid;
import com.crpc.example.common.model.User;
import com.crpc.example.common.service.UserService;
import com.crpc.springbootconsumer.service.DemoService;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {

    /**
     * 使用 Rpc 框架注入
     */
    @RpcAutoworid
    private UserService userService;

    @Override
    public void userTest() {
        User user = new User();
        user.setName("crpc");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}
