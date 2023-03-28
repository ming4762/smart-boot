package com.smart.service.system;

import com.smart.auth.core.temptoken.EnableAuthTempToken;
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
 * @author zhongming4762
 * 2023/3/28
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
public class SmartServiceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartServiceSystemApplication.class, args);
    }
}
