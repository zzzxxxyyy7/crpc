package com.zyj.example.provider;

import com.zyj.example.common.service.UserService;
import com.zyj.zyjrpc.bootstrap.ProviderBootstrap;
import com.zyj.zyjrpc.model.ServiceRegisterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务提供者示例
 *

 */
public class ProviderExample {

    public static void main(String[] args) {
        // 要注册的服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserService> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
