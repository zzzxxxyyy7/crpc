package com.zyj.zyjrpc.springboot.starter.bootstrap;

import com.zyj.zyjrpc.proxy.ServiceProxyFactory;
import com.zyj.zyjrpc.springboot.starter.annotation.RpcReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * Rpc 服务消费者启动
 */
@Slf4j
public class RpcConsumerBootstrap implements BeanPostProcessor {

    /**
     * Bean 初始化后执行，注入服务
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("exampleServiceImpl")) {
            System.out.println("开始执行自动注入");
            System.out.println("当前执行的Bean是：" + beanName);
        } else {
            return null;
        }
        Class<?> beanClass = bean.getClass();

        // 遍历对象的所有属性
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                // 为属性生成代理对象
                Class<?> interfaceClass = rpcReference.interfaceClass();
                if (interfaceClass == void.class) {
                    interfaceClass = field.getType();
                }
                field.setAccessible(true);
                Object proxyObject = ServiceProxyFactory.getProxy(interfaceClass);
                try {
                    field.set(bean, proxyObject);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("为字段注入代理对象失败", e);
                }
            }
        }
        System.out.println(BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName));
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}