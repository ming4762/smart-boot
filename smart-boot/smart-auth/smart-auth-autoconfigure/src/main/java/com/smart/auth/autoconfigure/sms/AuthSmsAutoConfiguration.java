package com.smart.auth.autoconfigure.sms;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.core.token.TokenRepository;
import com.smart.auth.extensions.sms.AuthSmsConfigure;
import com.smart.auth.extensions.sms.authentication.SmsAuthenticationProvider;
import com.smart.auth.extensions.sms.provider.DefaultSmsCreateValidateProviderImpl;
import com.smart.auth.extensions.sms.provider.SmsCreateValidateProvider;
import com.smart.auth.extensions.sms.userdetails.DefaultSmsUserDetailServiceImpl;
import com.smart.auth.extensions.sms.userdetails.SmsUserDetailService;
import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.system.SystemAuthUserApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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

    @Bean
    @ConditionalOnMissingBean
    public SmsCreateValidateProvider smsCreateValidateProvider(AuthCache<String, Object> authCache, SmartMessageApi smartMessageApi, AuthProperties authProperties) {
        return new DefaultSmsCreateValidateProviderImpl(authCache, smartMessageApi, authProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmsUserDetailService smsUserDetailService(List<TokenRepository> tokenRepositoryList, SystemAuthUserApi systemAuthUserApi) {
        return new DefaultSmsUserDetailServiceImpl(tokenRepositoryList, systemAuthUserApi);
    }
}
