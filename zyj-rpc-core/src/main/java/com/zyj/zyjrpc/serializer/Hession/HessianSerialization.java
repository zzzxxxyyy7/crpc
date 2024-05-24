package com.zyj.zyjrpc.serializer.Hession;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import com.zyj.zyjrpc.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian 序列化算法
 *
 */
public class HessianSerialization implements Serializer {
    @Override
    public <T> byte[] serialize(T object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HessianSerializerOutput hso = new HessianSerializerOutput(baos);
            hso.writeObject(object);
            hso.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            HessianSerializerInput hsi = new HessianSerializerInput(bis);
            return (T) hsi.readObject();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}