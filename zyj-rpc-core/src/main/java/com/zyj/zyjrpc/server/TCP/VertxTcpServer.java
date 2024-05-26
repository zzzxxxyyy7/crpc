package com.zyj.zyjrpc.server.TCP;

import com.zyj.zyjrpc.server.Server;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

/**
 * Vert TCP 服务器
 */
public class VertxTcpServer implements Server {
    private byte[] handlerequest(byte[] requestData) {
        return "Hello Client".getBytes();
    }

    @Override
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 通过 Vert.x实例 创建 TCP 服务器
        NetServer server = vertx.createNetServer();

        // 处理请求
        server.connectHandler(Socket -> {
            Socket.handler(buffer -> {
                // 处理接收到的字节数组
                byte[] requestData = buffer.getBytes();
                // 在这里对进行自定义的字节数组处理逻辑，如解析请求，调用服务，生成响应等
                byte[] responseData = handlerequest(requestData);
                // 发送响应
                Socket.write(Buffer.buffer(responseData));
            });
        });

        // 启动 TCP 服务器并监听端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port " + port);
            } else {
                System.out.println("Failed to start TCP server: " + result.cause());
            }
        });
    }
}