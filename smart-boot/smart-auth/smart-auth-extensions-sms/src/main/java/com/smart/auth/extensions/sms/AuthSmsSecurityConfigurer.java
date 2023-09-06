package com.smart.auth.extensions.sms;

import com.google.common.collect.Lists;
import com.smart.auth.extensions.sms.authentication.SmsAuthenticationProvider;
import com.smart.auth.extensions.sms.filter.SmsCodeCreateFilter;
import com.smart.auth.extensions.sms.filter.SmsLoginFilter;
import com.smart.auth.extensions.sms.provider.SmsCreateValidateProvider;
import com.smart.module.api.auth.AuthCaptchaApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author shizhongming
 * 2021/6/2 9:31 下午
 */
@Slf4j
public class AuthSmsSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

    private final ServiceProvider serviceProvider = new ServiceProvider();
    private static final String SMS_CREATE_CODE = "createCode";

    private static final String SMS_LOGIN = "login";

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
                .addFilterBefore(this.createLoginFilter(), BasicAuthenticationFilter.class);
    }

    /**
     * 创建登录相关拦截器
     * 1、创建验证码拦截器
     * 2、创建登录拦截器
     * @return 拦截器链
     */
    private FilterChainProxy createLoginFilter() {
        final List<SecurityFilterChain> chains = Lists.newArrayList();
        // 添加验证码创建拦截器
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(this.getUrl(SMS_CREATE_CODE)), this.createSmsCodeCreateFilter()));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(this.getUrl(SMS_LOGIN)), this.createSmsLoginFilter()));
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
    protected SmsLoginFilter createSmsLoginFilter() {
        final SmsLoginFilter smsLoginFilter = new SmsLoginFilter(this.getUrl(SMS_LOGIN));

        smsLoginFilter.setAuthenticationManager(this.getBuilder().getSharedObject(AuthenticationManager.class));
        // 设置登录成功handler
        smsLoginFilter.setAuthenticationSuccessHandler(this.getBean(AuthenticationSuccessHandler.class, this.serviceProvider.authenticationSuccessHandler));
        // 设置登录失败handler
        smsLoginFilter.setAuthenticationFailureHandler(this.getBean(AuthenticationFailureHandler.class, this.serviceProvider.authenticationFailureHandler));
        return smsLoginFilter;
    }

    private String getUrl(String url) {
        return this.serviceProvider.baseUrl + "/" + url;
    }

    private <T> T getBean(Class<T> clazz, T t) {
        if (Objects.nonNull(t)) {
            return t;
        }
        ApplicationContext applicationContext = this.getBuilder().getSharedObject(ApplicationContext.class);
        try {
            return Optional.ofNullable(applicationContext).map(item -> item.getBean(clazz)).orElse(null);
        } catch (NoSuchBeanDefinitionException e) {
            log.warn("获取bean发生错误: " + e.getMessage());
            return null;
        }
    }

    public AuthSmsSecurityConfigurer<H> baseUrl(String baseUrl) {
        this.serviceProvider.baseUrl = baseUrl;
        return this;
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

        private String baseUrl;

        private AuthenticationSuccessHandler authenticationSuccessHandler;

        private AuthenticationFailureHandler authenticationFailureHandler;

        public ServiceProvider() {
            this.baseUrl = "/auth/sms";
        }
    }
}
