package com.wangzhu.component.wrap;

import com.wangzhu.component.api.annotation.RpcComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by wang.zhu on 2021-05-28 19:51.
 **/
public class RpcComponentScanRegistrar implements ImportBeanDefinitionRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(RpcComponentScanRegistrar.class);

    private static final String BEAN_NAME = "rpcComponentScanRegisteringPostProcessor";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        final Set<String> packagesToScan = getPackageToScan(importingClassMetadata);
        final boolean flag = registry.containsBeanDefinition(BEAN_NAME);
        logger.info("detail flag|{}|packagesToScan|{}|BEAN_NAME|{}", flag, packagesToScan, BEAN_NAME);
        if (registry.containsBeanDefinition(BEAN_NAME)) {
            updatePostProcessor(registry, packagesToScan);
        } else {
            addPostProcessor(registry, packagesToScan);
        }
    }

    private Set<String> getPackageToScan(final AnnotationMetadata annotationMetadata) {
        final AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(RpcComponentScan.class.getName()));

        final Set<String> packagesToScan = new LinkedHashSet<>();

        final String[] basePackages = annotationAttributes.getStringArray("basePackages");
        for (final String basePackage : basePackages) {
            if (StringUtils.hasText(basePackage)) {
                packagesToScan.add(basePackage);
            }
        }

        final Class<?>[] basePackageClasses = annotationAttributes.getClassArray("basePackageClasses");
        for (final Class<?> basePackageClass : basePackageClasses) {
            packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
        }

        //没有，则使用当前类路径
        if (packagesToScan.isEmpty()) {
            packagesToScan.add(ClassUtils.getPackageName(annotationMetadata.getClassName()));
        }

        return packagesToScan;
    }

    private void addPostProcessor(final BeanDefinitionRegistry registry, final Set<String> packagesToScan) {
        final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RpcComponentScanRegisteringPostProcessor.class);
        beanDefinitionBuilder.addPropertyValue("packagesToScan", packagesToScan);
        registry.registerBeanDefinition(BEAN_NAME, beanDefinitionBuilder.getBeanDefinition());
    }

    private void updatePostProcessor(final BeanDefinitionRegistry registry, final Set<String> packagesToScan) {
        final BeanDefinition beanDefinition = registry.getBeanDefinition(BEAN_NAME);
        @SuppressWarnings("unchecked")
        final Set<String> mergedPackages = (Set<String>) beanDefinition.getPropertyValues().get("packagesToScan");
        mergedPackages.addAll(packagesToScan);
    }

}
