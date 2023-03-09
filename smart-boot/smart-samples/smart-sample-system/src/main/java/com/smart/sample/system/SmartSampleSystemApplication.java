package com.smart.sample.system;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.smart.auth.security.imports.EnableAuthTempToken;
import com.smart.autoconfigure.compatible.CompatibleAutoConfiguration;
import com.smart.commons.core.cors.EnableCors;
import com.smart.commons.core.spring.EnableRateLimit;
import com.smart.commons.core.spring.EnabledCustomObjectMapper;
import com.smart.commons.core.validate.EnableGlobalValidator;
import com.smart.i18n.config.EnableValidatorI18nSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * EnableRateLimit 开启限流
 * @author ShiZhongMing
 * 2022/1/11
 * @since 1.0
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableValidatorI18nSource
@EnableCors
@EnableGlobalValidator
@EnableAuthTempToken
@EnableRateLimit
@EnableCaching
@EnabledCustomObjectMapper
@CompatibleAutoConfiguration(exclude = {PageHelperAutoConfiguration.class})
public class SmartSampleSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartSampleSystemApplication.class, args);
    }
}
