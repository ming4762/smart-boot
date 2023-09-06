package com.smart.auth.security.config;

import com.smart.auth.core.service.AuthCache;
import com.smart.auth.security.filter.AuthCaptchaFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 验证码 spring security配置类
 * @author ShiZhongMing
 * 2022/1/22
 * @since 2.0.0
 */
@Slf4j
public class AuthCaptchaSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

    public static final String CREATE_URL = "/auth/createCaptcha";

    private AuthCaptchaSecurityConfigurer() {}

    private final ServiceProvider serviceProvider = new ServiceProvider();

    public static <H extends HttpSecurityBuilder<H>> AuthCaptchaSecurityConfigurer<H> captcha() {
        return new AuthCaptchaSecurityConfigurer<>();
    }


    public H config(Customizer<AuthCaptchaSecurityConfigurer<H>> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void configure(H builder) {
        builder.addFilterBefore(new AuthCaptchaFilter(this.serviceProvider.createUrl, this.serviceProvider.loginUrls, this.getBean(AuthCache.class, null)), BasicAuthenticationFilter.class);
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

    /**
     * 设置生成的URL
     * @param createUrl 创建验证码的URL
     * @return this
     */
    public AuthCaptchaSecurityConfigurer<H> createUrl(String createUrl) {
        this.serviceProvider.createUrl = createUrl;
        return this;
    }

    /**
     * 设置登录的URL
     * @param loginUrl 登录URL
     * @return this
     */
    public AuthCaptchaSecurityConfigurer<H> addLoginUrl(String loginUrl) {
        this.serviceProvider.loginUrls.add(loginUrl);
        return this;
    }

    private static class ServiceProvider {
        private String createUrl;

        private final List<String> loginUrls;

        public ServiceProvider() {
            this.loginUrls = new ArrayList<>(16);
            this.createUrl = CREATE_URL;
        }
    }
}
