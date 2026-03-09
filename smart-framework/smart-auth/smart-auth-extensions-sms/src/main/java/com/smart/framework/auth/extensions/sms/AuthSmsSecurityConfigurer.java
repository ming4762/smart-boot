package com.smart.framework.auth.extensions.sms;

import com.google.common.collect.Lists;
import com.smart.framework.auth.common.constants.AuthDomainConstants;
import com.smart.framework.auth.core.config.SmartAuthDomainConfig;
import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.core.constants.DefaultAuthUrlEnum;
import com.smart.framework.auth.extensions.sms.authentication.SmsAuthenticationProvider;
import com.smart.framework.auth.extensions.sms.filter.SmsCodeCreateFilter;
import com.smart.framework.auth.extensions.sms.filter.SmsLoginFilter;
import com.smart.framework.auth.extensions.sms.provider.SmsCreateValidateProvider;
import com.smart.module.api.auth.AuthCaptchaApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author shizhongming
 * 2021/6/2 9:31 下午
 */
@Slf4j
public class AuthSmsSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H, AuthSmsSecurityConfigurer<H>> {

    private final ServiceProvider serviceProvider = new ServiceProvider();

    private AuthSmsSecurityConfigurer() {}

    /**
     * SMS登录配置初始化
     * @return AuthSmsSecurityConfigurer
     */
    public static <H extends HttpSecurityBuilder<H>> AuthSmsSecurityConfigurer<H> sms() {
        return new AuthSmsSecurityConfigurer<>();
    }

    public H config(Customizer<AuthSmsSecurityConfigurer<H>> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void configure(H builder) {
        builder
                .authenticationProvider(this.getBean(SmsAuthenticationProvider.class, this.serviceProvider.authenticationProvider))
                .addFilterBefore(this.createLoginFilter(builder), BasicAuthenticationFilter.class);
    }

    /**
     * 创建登录相关拦截器
     * 1、创建验证码拦截器
     * 2、创建登录拦截器
     * @return 拦截器链
     */
    private FilterChainProxy createLoginFilter(H builder) {
        final List<SecurityFilterChain> chains = Lists.newArrayList();
        // 添加验证码创建拦截器
        chains.add(new DefaultSecurityFilterChain(PathPatternRequestMatcher.withDefaults().matcher(DefaultAuthUrlEnum.SMS_CREATE_CODE.getUrl()), this.createSmsCodeCreateFilter()));
        // 添加登录拦截器
        chains.addAll(this.createSmsLoginFilter(builder));
        return new FilterChainProxy(chains);
    }

    /**
     * 创建验证码生成拦截器
     * @return SmsCodeCreateFilter
     */
    protected SmsCodeCreateFilter createSmsCodeCreateFilter() {
        return new SmsCodeCreateFilter(this.getBean(SmsCreateValidateProvider.class, null), this.getBean(AuthCaptchaApi.class, null));
    }

    /**
     * 创建SMS登录拦截器
     * @return SMS登录拦截器
     */
    protected List<DefaultSecurityFilterChain> createSmsLoginFilter(H builder) {
        return this.getAuthDomainConfig().entrySet().stream()
                .map(item -> {
                    String authDomain = item.getKey();
                    SmartAuthDomainConfig authDomainConfig = item.getValue();
                    SmsLoginFilter smsLoginFilter = new SmsLoginFilter(authDomainConfig.getLoginUrl());

                    smsLoginFilter.setAuthenticationManager(this.getBuilder().getSharedObject(AuthenticationManager.class));
                    // 设置登录成功handler
                    smsLoginFilter.setAuthenticationSuccessHandler(Objects.requireNonNullElseGet(this.serviceProvider.authenticationSuccessHandler, () -> builder.getSharedObject(AuthenticationSuccessHandler.class)));
                    // 设置登录失败handler
                    smsLoginFilter.setAuthenticationFailureHandler(Objects.requireNonNullElseGet(this.serviceProvider.authenticationFailureHandler, () -> builder.getSharedObject(AuthenticationFailureHandler.class)));

                    smsLoginFilter.setAuthDomain(authDomain);
                    return new DefaultSecurityFilterChain(PathPatternRequestMatcher.withDefaults().matcher(authDomainConfig.getLoginUrl()), smsLoginFilter);
                }).toList();
    }

    /**
     * 获取权限域配置
     *
     * @return 获取权限域
     */
    @Override
    protected Map<String, SmartAuthDomainConfig> getAuthDomainConfig() {
        Map<String, SmartAuthDomainConfig> authDomainConfig = super.getAuthDomainConfig();
        if (!CollectionUtils.isEmpty(authDomainConfig)) {
            return authDomainConfig;
        }
        return Map.of(
                AuthDomainConstants.AUTH_DOMAIN_NONE,
                SmartAuthDomainConfig.builder()
                        .loginUrl(DefaultAuthUrlEnum.SMS_LOGIN.getUrl())
                        .build()
        );
    }

    public AuthSmsSecurityConfigurer<H> authenticationProvider(SmsAuthenticationProvider authenticationProvider) {
        this.serviceProvider.authenticationProvider = authenticationProvider;
        return this;
    }

    public AuthSmsSecurityConfigurer<H> authenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.serviceProvider.authenticationSuccessHandler = authenticationSuccessHandler;
        return this;
    }

    public AuthSmsSecurityConfigurer<H> authenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.serviceProvider.authenticationFailureHandler = authenticationFailureHandler;
        return this;
    }

    /**
     * 服务提供类
     */
    private static class ServiceProvider {

        private SmsAuthenticationProvider authenticationProvider;

        private AuthenticationSuccessHandler authenticationSuccessHandler;

        private AuthenticationFailureHandler authenticationFailureHandler;
    }
}
