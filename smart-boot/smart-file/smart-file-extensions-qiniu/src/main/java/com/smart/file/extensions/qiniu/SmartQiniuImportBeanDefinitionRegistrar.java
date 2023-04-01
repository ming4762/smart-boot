package com.smart.file.extensions.qiniu;

import com.smart.module.api.file.constants.FileStorageTypeEnum;
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
public class SmartQiniuImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        final BeanDefinition beanDefinition = new RootBeanDefinition(FileStorageQiniuServiceImpl .class);
        registry.registerBeanDefinition(FileStorageTypeEnum.QINIU.getServiceName(), beanDefinition);
    }
}
