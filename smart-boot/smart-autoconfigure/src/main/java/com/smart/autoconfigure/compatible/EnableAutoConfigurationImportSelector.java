package com.smart.autoconfigure.compatible;

import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * springboot3 不支持spring.factories中通过org.springframework.boot.autoconfigure.EnableAutoConfiguration配置自动配置类
 * 改为 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 进行配置， {@link AutoConfigurationImportSelector#getCandidateConfigurations(org.springframework.core.type.AnnotationMetadata, org.springframework.core.annotation.AnnotationAttributes)}
 * 添加该类实现对未升级库自动配置的支持
 * @author ShiZhongMing
 * 2022/9/30
 * @since 3.0.0
 */
public class EnableAutoConfigurationImportSelector extends AutoConfigurationImportSelector {

    @Override
    protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
        return SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, getBeanClassLoader());
    }

    @Override
    protected Class<?> getAnnotationClass() {
        return CompatibleAutoConfiguration.class;
    }
}
