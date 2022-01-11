package com.smart.file.extensions.disk;

import com.smart.commons.file.constants.ActualFileServiceEnum;
import com.smart.file.extensions.disk.service.ActualFileDiskServiceImpl;
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
public class DiskFileImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        final BeanDefinition beanDefinition = new RootBeanDefinition(ActualFileDiskServiceImpl.class);
        registry.registerBeanDefinition(ActualFileServiceEnum.DISK.getServiceName(), beanDefinition);
    }
}
