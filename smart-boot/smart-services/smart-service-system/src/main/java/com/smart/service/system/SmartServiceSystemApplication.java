package com.smart.service.system;

import com.smart.framework.auth.core.temptoken.EnableAuthTempToken;
import com.smart.framework.commons.core.cors.EnableCors;
import com.smart.framework.commons.core.spring.EnableRateLimit;
import com.smart.framework.commons.core.spring.EnabledCustomObjectMapper;
import com.smart.framework.commons.core.validate.EnableGlobalValidator;
import com.smart.framework.crud.spring.EnableMybatisPlusTenant;
import com.smart.framework.i18n.config.EnableValidatorI18nSource;
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
@EnableMybatisPlusTenant
public class SmartServiceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartServiceSystemApplication.class, args);
    }
}
