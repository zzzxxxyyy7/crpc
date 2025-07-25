package com.crpc.server;

/**
 * HTTP 服务器接口
 */
public interface Server {

    /**
     * 启动服务器
     *
     * @param port
     */
    void doStart(int port);
}
