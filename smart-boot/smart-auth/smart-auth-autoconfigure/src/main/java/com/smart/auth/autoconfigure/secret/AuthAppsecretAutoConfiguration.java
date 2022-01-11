package com.smart.auth.autoconfigure.secret;

import com.smart.auth.extensions.appsecret.AuthAppsecretSecurityConfigurer;
import com.smart.auth.extensions.appsecret.authentication.AppsecretAuthenticationProvider;
import com.smart.auth.extensions.appsecret.handler.AppLoginSuccessHandler;
import com.smart.auth.extensions.appsecret.service.AccessTokenCreator;
import com.smart.auth.extensions.appsecret.service.AppSecretService;
import com.smart.auth.extensions.appsecret.service.DefaultSha256AccessTokenCreator;
import com.smart.auth.extensions.appsecret.sign.DefaultSignProviderImpl;
import com.smart.auth.extensions.appsecret.sign.SignProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AuthAppsecretSecurityConfigurer.class)
public class AuthAppsecretAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AccessTokenCreator.class)
    public AccessTokenCreator defaultUuidAccessTokenCreator() {
        return new DefaultSha256AccessTokenCreator();
    }

    /**
     * 创建签名工具类
     * @return 签名工具类
     */
    @Bean
    @ConditionalOnMissingBean(SignProvider.class)
    public SignProvider defaultSignProvider() {
        return new DefaultSignProviderImpl();
    }

    /**
     * 创建认证服务类
     * @param appSecretService appSecretService
     * @return AppsecretAuthenticationProvider
     */
    @Bean
    @ConditionalOnMissingBean
    public AppsecretAuthenticationProvider appsecretAuthenticationProvider(AppSecretService appSecretService) {
        return new AppsecretAuthenticationProvider(appSecretService);
    }

    @Bean
    @ConditionalOnMissingBean
    public AppLoginSuccessHandler appLoginSuccessHandler() {
        return new AppLoginSuccessHandler();
    }

}
