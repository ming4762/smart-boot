package com.smart.cloud.code;

import com.smart.commons.core.spring.EnabledCustomObjectMapper;
import com.smart.commons.core.validate.EnableGlobalValidator;
import com.smart.i18n.config.EnableValidatorI18nSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 代码生成器微服务
 * @author zhongming4762
 * 2023/3/13
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableValidatorI18nSource
@EnableGlobalValidator
@EnabledCustomObjectMapper
@EnableFeignClients(basePackages = {"com.smart.cloud.api.system.feign", "com.smart.cloud.api.auth.feign"})
public class SmartCloudCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCloudCodeApplication.class, args);
    }
}