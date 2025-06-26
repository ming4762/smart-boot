package com.smart.cloud.service.system;

import com.smart.framework.commons.core.spring.EnabledCustomObjectMapper;
import com.smart.framework.commons.core.validate.EnableGlobalValidator;
import com.smart.framework.crud.spring.EnableMybatisPlusTenant;
import com.smart.framework.i18n.config.EnableValidatorI18nSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 系统模块微服务
 * @author zhongming4762
 * 2023/3/5
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableValidatorI18nSource
@EnableGlobalValidator
@EnabledCustomObjectMapper
@EnableMybatisPlusTenant
public class SmartCloudSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCloudSystemApplication.class, args);
    }
}
