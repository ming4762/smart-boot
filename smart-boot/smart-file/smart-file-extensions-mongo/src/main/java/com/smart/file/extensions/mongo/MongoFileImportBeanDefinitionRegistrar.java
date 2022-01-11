package com.smart.file.extensions.mongo;

import com.smart.commons.file.constants.ActualFileServiceEnum;
import com.smart.file.extensions.mongo.service.ActualFileServiceMongoImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * 注册
 * @author shizhongming
 * 2020/12/7 10:30 下午
 */
public class MongoFileImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        final BeanDefinition beanDefinition = new RootBeanDefinition(ActualFileServiceMongoImpl.class);
        registry.registerBeanDefinition(ActualFileServiceEnum.MONGO.getServiceName(), beanDefinition);
    }
}
