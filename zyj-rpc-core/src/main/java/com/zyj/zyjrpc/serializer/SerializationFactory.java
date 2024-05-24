package com.zyj.zyjrpc.serializer;

import com.zyj.zyjrpc.constant.RpcConstant;
import com.zyj.zyjrpc.serializer.Hession.HessianSerialization;
import com.zyj.zyjrpc.serializer.JDK.JdkSerializer;
import com.zyj.zyjrpc.serializer.JSON.JsonSerialization;
import com.zyj.zyjrpc.serializer.KRYO.KryoSerialization;

/**
 * 序列化算法工厂，通过序列化枚举类型获取相应的序列化算法实例
 *
 */
public class SerializationFactory {
    public static Serializer getSerialization(String enumType) {
        switch (enumType) {
            case RpcConstant.JKD:
                return new JdkSerializer();
            case RpcConstant.JSON:
                return new JsonSerialization();
            case RpcConstant.Hession:
                return new HessianSerialization();
            case RpcConstant.KRYO:
                return new KryoSerialization();
            default:
                throw new IllegalArgumentException(
                                String.format("The serialization type %s is illegal.", enumType));
        }
    }
}