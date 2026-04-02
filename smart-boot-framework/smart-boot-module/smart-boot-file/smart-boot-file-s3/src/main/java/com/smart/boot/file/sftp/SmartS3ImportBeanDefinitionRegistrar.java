package com.smart.boot.file.sftp;

import com.smart.framework.file.extensions.s3.DefaultAmazonS3ServiceImpl;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author zhongming4762
 * 2022/12/31 21:27
 */
public class SmartS3ImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        final BeanDefinition beanDefinition = new RootBeanDefinition(DefaultAmazonS3ServiceImpl.class);
        registry.registerBeanDefinition(FileStorageTypeEnum.AMAZON_S3.getServiceName(), beanDefinition);
    }
}
