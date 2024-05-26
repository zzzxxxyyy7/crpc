package com.zyj.zyjrpc.protocol;

import com.zyj.zyjrpc.serializer.SerializationFactory;
import com.zyj.zyjrpc.serializer.Serializer;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * 协议消息编码器
 */
public class ProtocolMessageEncoder {

    /**
     * 编码
     * @param protocolMessage
     * @return
     * @throws IOException
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
        if (protocolMessage == null || protocolMessage.getHeader() == null) {
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = protocolMessage.getHeader();
        // 依次向缓冲区写入字节
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());
        // 获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("序列化协议不存在");
        }
        // 通过序列化工厂获得指定序列化器
        Serializer serializer = SerializationFactory.getSerialization(serializerEnum.getValue());
        // 把请求编码为byte数组
        byte[] bodyBytes = serializer.serialize(protocolMessage.getBody());
        // 写入 body 长度和数据
        buffer.appendInt(bodyBytes.length);
        // 往 Buffer 中写入请求体数据(Byte数组)
        buffer.appendBytes(bodyBytes);
        // 往下传递 Buffer 数组
        return buffer;
    }
}