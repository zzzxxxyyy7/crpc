package com.zyj.zyjrpc.config;

import com.zyj.zyjrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * 默认值
 */
@Data
public class RpcConfig {
    /**
     * 默认服务名称
     */
    private String name = "zyj-rpc";

    /**
     * 默认版本号
     */
    private String version = "1.0";

    /**
     * 默认地址
     */
    private String host = "localhost";

    /**
     * 默认端口
     */
    private Integer serverPort = 8080;

    /**
     * 模拟调用
     */
    private Boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();
}