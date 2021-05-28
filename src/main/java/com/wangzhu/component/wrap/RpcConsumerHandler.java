package com.wangzhu.component.wrap;

import com.wangzhu.component.api.annotation.RpcConsumer;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created by wang.zhu on 2021-05-28 19:58.
 **/
public class RpcConsumerHandler extends RpcComponentHandler {

    public RpcConsumerHandler(final ApplicationContext applicationContext) {
        super(applicationContext, RpcConsumer.class);
    }

    @Override
    protected void doHandler(Map<String, Object> attributes, AnnotatedBeanDefinition annotatedBeanDefinition, ConfigurableListableBeanFactory beanFactory) {
        final String className = annotatedBeanDefinition.getBeanClassName();
        final BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        logger.info("详情 annotatedBeanDefinition|{}|registry|{}|attributes|{}|className|{}", annotatedBeanDefinition, registry, attributes, className);

        //使用构造器构造对象
        //否则第一次getObjectType时，可能还没有调用InitializingBean，导致异常
        final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RpcConsumerBean.class);
        beanDefinitionBuilder.addConstructorArgValue(attributes.get("uri"));
        beanDefinitionBuilder.addConstructorArgValue(annotatedBeanDefinition.getBeanClassName());
        beanDefinitionBuilder.addConstructorArgValue(attributes.get("timeout"));

        final String beanName = MapUtils.getString(attributes, "name");
        registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }
}
