package com.smart.file.extensions.sftp;

import com.smart.file.core.constants.FileStorageTypeEnum;
import com.smart.file.extensions.sftp.service.FileStorageNfsServiceImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * ActualFileServiceNfsImpl 注册类
 * @author shizhongming
 * 2020/12/7 10:41 下午
 */
public class FileNfsImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        final BeanDefinition beanDefinition = new RootBeanDefinition(FileStorageNfsServiceImpl .class);
        registry.registerBeanDefinition(FileStorageTypeEnum.SFTP.getServiceName(), beanDefinition);
    }
}
