package com.smart.autoconfigure.file.minio;

import com.smart.file.core.SmartFileProperties;
import com.smart.file.extensions.minio.MinioService;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhongming4762
 * 2022/12/31 21:21
 */
@Configuration
@Import(SmartMinioImportBeanDefinitionRegistrar.class)
@ConditionalOnClass(MinioService.class)
public class SmartFileMinioAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(MinioClient.class)
    public MinioClient minioClient(SmartFileProperties properties) {
        SmartFileProperties.MinioProperties minioProperties = properties.getMinio();
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}
