package com.smart.file.extensions.nfs;

import com.smart.commons.file.constants.ActualFileServiceEnum;
import com.smart.file.extensions.nfs.service.ActualFileServiceNfsImpl;
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
        final BeanDefinition beanDefinition = new RootBeanDefinition(ActualFileServiceNfsImpl.class);
        registry.registerBeanDefinition(ActualFileServiceEnum.NFS.getServiceName(), beanDefinition);
    }
}
