package com.smart.auth.security.config;

import com.smart.auth.core.service.AuthCache;
import com.smart.auth.security.filter.AuthCaptchaFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;
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
public class AuthCaptchaSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    public static final String CREATE_URL = "/auth/createCaptcha";

    private AuthCaptchaSecurityConfigurer() {}

    private final ServiceProvider serviceProvider = new ServiceProvider();

    public static AuthCaptchaSecurityConfigurer captcha() {
        return new AuthCaptchaSecurityConfigurer();
    }

    @Override
    public void init(HttpSecurity builder) throws Exception {
        builder.addFilterBefore(new AuthCaptchaFilter(Optional.ofNullable(this.serviceProvider.createUrl).orElse(CREATE_URL), this.serviceProvider.loginUrls, this.getBean(AuthCache.class, null)), BasicAuthenticationFilter.class);
    }

    private <T> T getBean(Class<T> clazz, T t) {
        if (Objects.nonNull(t)) {
            return t;
        }
        try {
            return Optional.ofNullable(this.serviceProvider.applicationContext).map(item -> item.getBean(clazz)).orElse(null);
        } catch (NoSuchBeanDefinitionException e) {
            log.warn("获取bean发生错误: " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取服务配置类
     * @return 服务配置类
     */
    public ServiceProvider serviceProvider() {
        return this.serviceProvider;
    }

    public static class ServiceProvider {
        private String createUrl;

        private List<String> loginUrls;

        private ApplicationContext applicationContext;

        public ServiceProvider applicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
            return this;
        }

        public ServiceProvider createUrl(String createUrl) {
            this.createUrl = createUrl;
            return this;
        }

        public ServiceProvider loginUrl(String ...loginUrls) {
            this.loginUrls = Arrays.asList(loginUrls);
            return this;
        }
    }
}
