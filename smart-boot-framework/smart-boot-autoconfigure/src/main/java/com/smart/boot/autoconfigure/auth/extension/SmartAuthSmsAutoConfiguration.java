package com.smart.boot.autoconfigure.auth.extension;

import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.core.userdetails.UserDetailsBuilder;
import com.smart.framework.auth.extensions.sms.AuthSmsSecurityConfigurer;
import com.smart.framework.auth.extensions.sms.authentication.SmsAuthenticationProvider;
import com.smart.framework.auth.extensions.sms.provider.DefaultSmsCreateValidateProviderImpl;
import com.smart.framework.auth.extensions.sms.provider.SmsCreateValidateProvider;
import com.smart.framework.auth.extensions.sms.userdetails.DefaultSmsUserDetailServiceImpl;
import com.smart.framework.auth.extensions.sms.userdetails.SmsUserDetailService;
import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.system.SystemAuthUserApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2024/9/11 14:58
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AuthSmsSecurityConfigurer.class)
public class SmartAuthSmsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmsAuthenticationProvider smsAuthenticationProvider(SmsUserDetailService smsUserDetailService, SmsCreateValidateProvider smsCreateValidateProvider) {
        return new SmsAuthenticationProvider(smsUserDetailService, smsCreateValidateProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmsCreateValidateProvider smsCreateValidateProvider(AuthCache authCache, SmartMessageApi smartMessageApi, AuthProperties authProperties) {
        return new DefaultSmsCreateValidateProviderImpl(authCache, smartMessageApi, authProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmsUserDetailService smsUserDetailService(SystemAuthUserApi systemAuthUserApi, UserDetailsBuilder userDetailsBuilder) {
        return new DefaultSmsUserDetailServiceImpl(systemAuthUserApi, userDetailsBuilder);
    }
}
