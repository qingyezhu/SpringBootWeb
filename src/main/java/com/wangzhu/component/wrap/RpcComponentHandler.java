package com.wangzhu.component.wrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Created by wang.zhu on 2021-05-28 19:56.
 **/
public abstract class RpcComponentHandler {
    protected static final Logger logger = LoggerFactory.getLogger(RpcComponentHandler.class);

    protected final ApplicationContext applicationContext;

    private final Class<? extends Annotation> annotationType;
    private final TypeFilter typeFilter;

    protected RpcComponentHandler(final ApplicationContext applicationContext, final Class<? extends Annotation> annotationType) {
        this.applicationContext = applicationContext;
        this.annotationType = annotationType;
        this.typeFilter = new AnnotationTypeFilter(annotationType);
    }

    TypeFilter getTypeFilter() {
        return typeFilter;
    }

    void handler(final AnnotatedBeanDefinition annotatedBeanDefinition, final ConfigurableListableBeanFactory beanFactory) {
        final Map<String, Object> attributes = annotatedBeanDefinition.getMetadata().getAnnotationAttributes(annotationType.getName());
        if (attributes != null) {
            doHandler(attributes, annotatedBeanDefinition, beanFactory);
        }
    }

    protected abstract void doHandler(Map<String, Object> attributes, AnnotatedBeanDefinition annotatedBeanDefinition, ConfigurableListableBeanFactory beanFactory);
}
