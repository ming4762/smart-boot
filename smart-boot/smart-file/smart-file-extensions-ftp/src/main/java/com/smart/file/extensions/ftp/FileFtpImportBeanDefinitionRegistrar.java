package com.smart.file.extensions.ftp;

import com.smart.file.extensions.ftp.service.FileStorageFtpServiceImpl;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * FTP服务注册类
 * @author shizhongming
 * 2023/3/21 17:28
 * @since 3.0.0
 */
public class FileFtpImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        final BeanDefinition beanDefinition = new RootBeanDefinition(FileStorageFtpServiceImpl.class);
        registry.registerBeanDefinition(FileStorageTypeEnum.FTP.getServiceName(), beanDefinition);
    }
}
