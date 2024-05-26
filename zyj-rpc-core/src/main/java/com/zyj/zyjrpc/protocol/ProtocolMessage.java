package com.zyj.zyjrpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 协议消息结构
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体（请求或相应对象）
     */
    private T body;

    /**
     * 协议消息头
     */
    @Data
    public static class Header {
        /**
         * 魔数
         */
        private byte magic;
        /**
         * 版本号
         */
        private byte version;
        /**
         * 序列化器
         */
        private byte serializer;
        /**
         * 消息类型（请求或响应）
         */
        private byte type;
        /**
         * 状态码
         */
        private byte status;
        /**
         * 请求ID
         */
        private long requestId;
        /**
         * 消息体长度
         */
        private int bodyLength;
    }
}