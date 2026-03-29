package com.smart.boot.crud;

import com.smart.framework.commons.core.spring.EnableApplicationContext;
import com.smart.framework.crud.desensitization.handler.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 脱敏自动配置
 * @author shizhongming
 * 2025/1/10 14:01
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableApplicationContext
public class SmartDesensitizeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BankCardDesensitizeHandler bankCardDesensitizeHandler() {
        return new BankCardDesensitizeHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public EmailDesensitizeHandler emailDesensitizeHandler() {
        return new EmailDesensitizeHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public EncodeDesensitizeHandler encodeDesensitizeHandler() {
        return new EncodeDesensitizeHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public IdCardDesensitizeHandler idCardDesensitizeHandler() {
        return new IdCardDesensitizeHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public PhoneDesensitizeHandler phoneDesensitizeHandler() {
        return new PhoneDesensitizeHandler();
    }
}
