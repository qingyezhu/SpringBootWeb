package com.wangzhu;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

/**
 * Created by wang.zhu on 2020-11-02 20:33.
 **/
public class Handler implements ApplicationContextAware, BeanFactoryAware, InitializingBean, Ordered {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        final String[] beanNames = applicationContext.getBeanDefinitionNames();
////                applicationContext.getBeanNamesForAnnotation(Service.class);
//        for (String beanName : beanNames) {
//            System.out.println("------" + beanName);
//            Object object = applicationContext.getBean(beanName);
//            System.out.println(object);
//            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
//            System.out.println("==" + beanDefinition.getBeanClassName());
//            System.out.println(beanFactory.getType(beanName));
//            if (beanDefinition instanceof AbstractBeanDefinition) {
//                AbstractBeanDefinition genericBeanDefinition = (AbstractBeanDefinition) beanDefinition;
//                if (genericBeanDefinition.hasBeanClass()) {
//                    System.out.println("====" + genericBeanDefinition.getBeanClass());
//                }
//            }
//
//        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1000;
    }
}
