package com.smart.file.extensions.s3;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * S3文件自动配置类
 * @author shizhongming
 * 2024/4/24 20:32
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
@Import(SmartS3ImportBeanDefinitionRegistrar.class)
public class SmartFileS3AutoConfiguration {
}
