package com.zyj.zyjrpc.serializer;

import com.zyj.zyjrpc.serializer.JDK.JdkSerializer;
import com.zyj.zyjrpc.spi.SpiLoader;

/**
 * 序列化算法工厂，通过序列化枚举类型获取相应的序列化算法实例
 *
 */
public class SerializationFactory {
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZATION = new JdkSerializer();

    public static Serializer getSerialization(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}