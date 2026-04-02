package com.smart.boot.file.sftp;

import com.smart.framework.file.extensions.s3.AmazonS3Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(AmazonS3Service.class)
public class SmartFileS3AutoConfiguration {
}
