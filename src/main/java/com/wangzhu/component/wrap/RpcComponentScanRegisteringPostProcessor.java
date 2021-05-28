package com.wangzhu.component.wrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by wang.zhu on 2021-05-28 19:53.
 **/
public class RpcComponentScanRegisteringPostProcessor implements ApplicationContextAware, InitializingBean, BeanFactoryPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(RpcComponentScanRegisteringPostProcessor.class);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Set<String> packagesToScan;

    public void setPackagesToScan(Set<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    List<RpcComponentHandler> handlers;

    @Override
    public void afterPropertiesSet() throws Exception {
        final List<RpcComponentHandler> handlers = new ArrayList<>();
        handlers.add(new RpcProviderHandler(applicationContext));
        handlers.add(new RpcConsumerHandler(applicationContext));
        this.handlers = Collections.unmodifiableList(handlers);
        logger.info("packagesToScan|{}|handlers|{}", this.packagesToScan, this.handlers);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        logger.info("packagesToScan|{}", packagesToScan);
        final ClassPathScanningCandidateComponentProvider scanner = getScanner();
        for (final String packageToScan : packagesToScan) {
            scanPackage(beanFactory, scanner, packageToScan);
        }
    }

    private ClassPathScanningCandidateComponentProvider getScanner() {
        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {

            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                AnnotationMetadata metadata = beanDefinition.getMetadata();
                return metadata.isIndependent() && metadata.isInterface();
            }
        };
        scanner.setEnvironment(this.applicationContext.getEnvironment());
        scanner.setResourceLoader(this.applicationContext);
        handlers.forEach(businessComponentHandler -> scanner.addIncludeFilter(businessComponentHandler.getTypeFilter()));
        return scanner;
    }

    private void scanPackage(final ConfigurableListableBeanFactory beanFactory, final ClassPathScanningCandidateComponentProvider scanner, final String packageToScan) {
        final Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(packageToScan);
        logger.info("detail packageToScan|{}|beanDefinitions|{}", packageToScan, beanDefinitions);
        for (final BeanDefinition candidate : beanDefinitions) {
            if (candidate instanceof AnnotatedBeanDefinition) {
                handlers.forEach(businessComponentHandler -> businessComponentHandler.handler((AnnotatedBeanDefinition) candidate, beanFactory));
            }
        }
    }
}
