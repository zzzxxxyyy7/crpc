package com.zyj.zyjrpc.config;

import lombok.Data;

/**
 * 默认值
 */
@Data
public class RpcConfig {

    private String name = "zyj-rpc";

    private String version = "1.0";

    private String host = "localhost";

    private Integer serverPort = 8080;

    /**
     * 模拟调用
     */
    private Boolean mock = false;
}