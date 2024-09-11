package com.smart.auth.extensions.sms;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.core.userdetails.UserDetailsBuilder;
import com.smart.auth.extensions.sms.authentication.SmsAuthenticationProvider;
import com.smart.auth.extensions.sms.provider.DefaultSmsCreateValidateProviderImpl;
import com.smart.auth.extensions.sms.provider.SmsCreateValidateProvider;
import com.smart.auth.extensions.sms.userdetails.DefaultSmsUserDetailServiceImpl;
import com.smart.auth.extensions.sms.userdetails.SmsUserDetailService;
import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.system.SystemAuthUserApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2024/9/11 14:58
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SmartAuthSmsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmsAuthenticationProvider smsAuthenticationProvider(SmsUserDetailService smsUserDetailService, SmsCreateValidateProvider smsCreateValidateProvider) {
        return new SmsAuthenticationProvider(smsUserDetailService, smsCreateValidateProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmsCreateValidateProvider smsCreateValidateProvider(AuthCache<String, Object> authCache, SmartMessageApi smartMessageApi, AuthProperties authProperties) {
        return new DefaultSmsCreateValidateProviderImpl(authCache, smartMessageApi, authProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmsUserDetailService smsUserDetailService(SystemAuthUserApi systemAuthUserApi, UserDetailsBuilder userDetailsBuilder) {
        return new DefaultSmsUserDetailServiceImpl(systemAuthUserApi, userDetailsBuilder);
    }
}
