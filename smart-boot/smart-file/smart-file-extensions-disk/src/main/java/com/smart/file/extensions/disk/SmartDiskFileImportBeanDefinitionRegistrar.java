package com.smart.file.extensions.disk;

import com.smart.file.extensions.disk.service.FileStorageDiskServiceImpl;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * @author shizhongming
 * 2020/12/7 10:36 下午
 */
public class SmartDiskFileImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        final BeanDefinition beanDefinition = new RootBeanDefinition(FileStorageDiskServiceImpl.class);
        registry.registerBeanDefinition(FileStorageTypeEnum.DISK.getServiceName(), beanDefinition);
    }
}
