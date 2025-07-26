package com.crpc.springbootconsumer;

import com.crpc.crpc.springboot.starter.annotation.RpcAutoworid;
import com.crpc.example.common.model.User;
import com.crpc.example.common.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * 示例服务实现类
 */
@Service
public class ExampleServiceImpl implements InitializingBean {

    /**
     * 使用 Rpc 框架注入
     */
    @RpcAutoworid
    private UserService userService;

    @Override
    public void afterPropertiesSet() {
        User user = new User();
        user.setName("crpc");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}
