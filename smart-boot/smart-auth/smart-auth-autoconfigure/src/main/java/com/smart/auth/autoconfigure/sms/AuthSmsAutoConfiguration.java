package com.smart.auth.autoconfigure.sms;

import com.smart.auth.core.userdetails.SmsUserDetailService;
import com.smart.auth.extensions.sms.AuthSmsConfigure;
import com.smart.auth.extensions.sms.authentication.SmsAuthenticationProvider;
import com.smart.auth.extensions.sms.provider.SmsCreateValidateProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/6/4 13:02
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AuthSmsConfigure.class)
public class AuthSmsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmsAuthenticationProvider smsAuthenticationProvider(SmsUserDetailService smsUserDetailService, SmsCreateValidateProvider smsCreateValidateProvider) {
        return new SmsAuthenticationProvider(smsUserDetailService, smsCreateValidateProvider);
    }
}
