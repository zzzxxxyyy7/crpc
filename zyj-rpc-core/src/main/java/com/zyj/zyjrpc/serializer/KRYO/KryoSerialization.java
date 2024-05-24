package com.zyj.zyjrpc.serializer.KRYO;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.zyj.zyjrpc.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Kryo 序列化算法
 * <p>
 * <a href="https://www.cnblogs.com/lxyit/p/12511645.html">相关简介</a><br>
 * <a href="https://github.com/EsotericSoftware/kryo">github地址</a>
 * </p>
 *
 * @author zyj
 * @version 1.0
 * @ClassName KryoSerialization
 */
public class KryoSerialization implements Serializer {

    // kryo 线程不安全，所以使用 ThreadLocal 保存 kryo 对象
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public <T> byte[] serialize(T object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Output output = new Output(baos);
            Kryo kryo = kryoThreadLocal.get();
            // 将对象序列化为 byte 数组
            kryo.writeObject(output, object);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Input input = new Input(bais);
            Kryo kryo = kryoThreadLocal.get();
            // 将 byte 数组反序列化为 T 对象
            T object = kryo.readObject(input, type);
            kryoThreadLocal.remove();
            return object;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
