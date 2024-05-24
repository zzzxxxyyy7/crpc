package com.zyj.zyjrpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock 服务代理（JDK动态代理）
 */
public class MockServiceProxy implements InvocationHandler {
    // TODO 学习Java动态代理

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        return getdefaultobject(returnType);
    }

    private Object getdefaultobject(Class<?> returnType) {
        if(returnType.isPrimitive()) {
            if(returnType == boolean.class) {
                return false;
            } else if(returnType == short.class) {
                return (short) 0;
            } else if (returnType == int.class) {
                return 0;
            } else if (returnType == long.class) {
                return 0L;
            }
        }
        return null;
    }
}