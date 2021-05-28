package com.wangzhu.component.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 服务代理工厂<br/>
 * Created by wang.zhu on 2021-05-28 19:38.
 **/
public class ServiceProxyFactory implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProxyFactory.class);

    private String serviceInterface;

    private ServiceProxyProcessor serviceProxyProcessor;

    private Class<?> serviceClazz;

    public String getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public ServiceProxyProcessor getServiceProxyProcessor() {
        return serviceProxyProcessor;
    }

    public void setServiceProxyProcessor(ServiceProxyProcessor serviceProxyProcessor) {
        this.serviceProxyProcessor = serviceProxyProcessor;
    }

    public ServiceProxyFactory(String serviceInterface, ServiceProxyProcessor serviceProxyProcessor) {
        this.serviceInterface = serviceInterface;
        this.serviceProxyProcessor = serviceProxyProcessor;
    }

    public void init() {
        try {
            this.serviceClazz = Class.forName(serviceInterface);
        } catch (ClassNotFoundException e) {
            logger.error(serviceInterface + " 接口加载失败: 接口没有找到", e);
        }
        logger.info("详情 serviceInterface|{}|serviceClazz|{}", this.serviceInterface, this.serviceClazz);
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(ServiceProxyFactory.class.getClassLoader(), new Class[]{serviceClazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String methodName = method.getName();
        if ("toString".equals(methodName)) {
            return proxy.getClass().toString();
        }
        return serviceProxyProcessor.invoke(methodName, args);
    }
}