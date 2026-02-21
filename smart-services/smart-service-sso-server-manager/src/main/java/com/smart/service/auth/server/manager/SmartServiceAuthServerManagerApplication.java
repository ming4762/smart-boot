package com.smart.service.auth.server.manager;

import com.smart.framework.auth.core.temptoken.EnableAuthTempToken;
import com.smart.framework.commons.core.cors.EnableCors;
import com.smart.framework.commons.core.spring.*;
import com.smart.framework.commons.core.validate.EnableGlobalValidator;
import com.smart.framework.crud.spring.EnableMybatisPlusTenant;
import com.smart.framework.i18n.config.EnableValidatorI18nSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 单点登录认证服务器
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/9 20:27
 * @since 5.0.0
 */
@SpringBootApplication
@EnableValidatorI18nSource
@EnableCors
@EnableGlobalValidator
@EnableAuthTempToken
@EnableRateLimit
@EnableCaching
@EnabledCustomObjectMapper
@EnableMybatisPlusTenant
@EnableApplicationContext
@EnableMultiTimeZone
@EnableRest
public class SmartServiceAuthServerManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartServiceAuthServerManagerApplication.class, args);
    }
}
