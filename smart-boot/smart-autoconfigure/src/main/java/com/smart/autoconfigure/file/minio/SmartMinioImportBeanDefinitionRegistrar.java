package com.smart.autoconfigure.file.minio;

import com.smart.file.core.constants.ActualFileServiceEnum;
import com.smart.file.extensions.minio.DefaultMinioServiceImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * @author zhongming4762
 * 2022/12/31 21:27
 */
public class SmartMinioImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        final BeanDefinition beanDefinition = new RootBeanDefinition(DefaultMinioServiceImpl.class);
        registry.registerBeanDefinition(ActualFileServiceEnum.MINIO.getServiceName(), beanDefinition);
    }
}
