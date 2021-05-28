package com.wangzhu.component.proxy;

import com.wangzhu.component.api.annotation.ProxyMethod;
import com.wangzhu.component.api.annotation.ProxyService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务代理处理器<br/>
 * Created by wang.zhu on 2021-05-28 19:29.
 **/
public class ServiceProxyProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProxyProcessor.class);

    private Map<String, Pair<Method, Object>> methods = new HashMap<>();

    public Object invoke(final String methodName, final Object[] args) throws Throwable {
        final Pair<Method, Object> pair = methods.get(methodName);
        if (pair == null) {
            logger.error("方法未找到 methodName|{}|args|{}", methodName, Arrays.toString(args));
            throw new UnsupportedOperationException("未添加实现的方法 methodName|" + methodName + "|args|{1}" + Arrays.toString(args));
        }
        final Method method = pair.getKey();
        final Object object = pair.getValue();
        return method.invoke(object, args);
    }

    private void addMethods(final Object object, final String beanName) {
        final Class<?> clazz = object.getClass();
        final ProxyService proxyService = AnnotationUtils.findAnnotation(clazz, ProxyService.class);
        if (logger.isDebugEnabled()) {
            logger.debug("详情 beanName|{}|clazz|{}|proxyService|{}", beanName, clazz, proxyService);
        }
        if (proxyService == null) {
            return;
        }
        for (final Method method : clazz.getMethods()) {
            final String methodName = method.getName();
            final ProxyMethod proxyMethod = AnnotationUtils.findAnnotation(method, ProxyMethod.class);
            if (logger.isDebugEnabled()) {
                logger.debug("详情 beanName|{}|methodName|{}|clazz|{}|method|{}|proxyMethod|{}|isAbstract|{}|isInterface|{}|isAopProxy|{}|isProxyClass|{}",
                        beanName, methodName, clazz, method, proxyMethod, Modifier.isAbstract(method.getModifiers()), Modifier.isInterface(method.getModifiers()), AopUtils.isAopProxy(object), Proxy.isProxyClass(clazz));
            }
            if (proxyMethod == null) {
                continue;
            }
            logger.info("服务代理方法初始化 beanName|{}|methodName|{}|clazz|{}|method|{}", beanName, methodName, clazz, method);
            if (methods.put(methodName, ImmutablePair.of(method, object)) != null) {
                logger.warn("方法名重复了 beanName|{}|methodName|{}|clazz|{}|method|{}", beanName, methodName, clazz, method);
            }
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        addMethods(bean, beanName);
        return bean;
    }
}