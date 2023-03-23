package com.smart.cloud.file;

import com.smart.commons.core.spring.EnabledCustomObjectMapper;
import com.smart.commons.core.validate.EnableGlobalValidator;
import com.smart.i18n.config.EnableValidatorI18nSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zhongming4762
 * 2023/3/21
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableGlobalValidator
@EnabledCustomObjectMapper
@EnableTransactionManagement
@EnableValidatorI18nSource
@EnableFeignClients(basePackages = {"com.smart.cloud.api.auth.feign", "com.smart.cloud.api.system.feign"})
public class SmartCloudFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCloudFileApplication.class, args);
    }
}
