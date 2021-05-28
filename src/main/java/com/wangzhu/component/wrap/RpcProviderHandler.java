package com.wangzhu.component.wrap;

import com.wangzhu.component.api.annotation.RpcProvider;
import com.wangzhu.component.bean.RpcProviderBean;
import com.wangzhu.component.proxy.ServiceProxyFactory;
import com.wangzhu.component.proxy.ServiceProxyProcessor;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created by wang.zhu on 2021-05-28 20:00.
 **/
public class RpcProviderHandler extends RpcComponentHandler {
    public RpcProviderHandler(final ApplicationContext applicationContext) {
        super(applicationContext, RpcProvider.class);
    }


    private void setProperty(final Map<String, Object> attributes) {
        //todo
    }

    @Override
    protected void doHandler(Map<String, Object> attributes, AnnotatedBeanDefinition annotatedBeanDefinition, ConfigurableListableBeanFactory beanFactory) {
        final String className = annotatedBeanDefinition.getBeanClassName();
        final BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

        final String uri = MapUtils.getString(attributes, "uri");

        //根据环境配置不同的服务名 todo
        final String serviceUri = uri;

        final String tmpServiceUri = applicationContext.getEnvironment().getProperty("serviceUri");
        logger.info("详情 className|{}|uri|{}|serviceUri|{}|tmpServiceUri|{}", className, uri, serviceUri, tmpServiceUri);
        if (!serviceUri.equals(tmpServiceUri)) {
            logger.warn("缺少环境配置服务名 className|{}|uri|{}", className, uri);
            return;
        }

        logger.info("详情 annotatedBeanDefinition|{}|registry|{}|attributes|{}|className|{}", annotatedBeanDefinition, registry, attributes, className);

        //注入环境变量
        setProperty((attributes));

        final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RpcProviderBean.class);
        beanDefinitionBuilder.addPropertyValue("serviceUri", serviceUri);
        beanDefinitionBuilder.addPropertyValue("serviceInterface", className);
        beanDefinitionBuilder.addPropertyValue("timeout", attributes.get("timeout"));

        //初始化代理方法
        final ServiceProxyProcessor serviceProxyProcessor = new ServiceProxyProcessor();
        beanFactory.addBeanPostProcessor(serviceProxyProcessor);

        //创建代理对象
        final BeanDefinitionBuilder proxyBeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ServiceProxyFactory.class);
        proxyBeanDefinitionBuilder.addPropertyValue("serviceProxyProcessor", serviceProxyProcessor);
        proxyBeanDefinitionBuilder.addPropertyValue("serviceInterface", className);

        //注入对外的服务
        beanDefinitionBuilder.addPropertyValue("instance", proxyBeanDefinitionBuilder.getBeanDefinition());

        final String beanName = MapUtils.getString(attributes, "name");
        registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }
}