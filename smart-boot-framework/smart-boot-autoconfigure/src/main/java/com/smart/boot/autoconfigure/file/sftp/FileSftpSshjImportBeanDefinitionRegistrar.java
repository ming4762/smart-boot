package com.smart.boot.autoconfigure.file.sftp;

import com.smart.framework.file.extensions.sftp.sshj.service.FileStorageSftpSshjServiceImpl;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
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
public class FileSftpSshjImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        final BeanDefinition beanDefinition = new RootBeanDefinition(FileStorageSftpSshjServiceImpl.class);
        registry.registerBeanDefinition(FileStorageTypeEnum.SFTP_SSHJ.getServiceName(), beanDefinition);
    }
}
