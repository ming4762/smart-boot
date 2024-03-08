package com.smart.auth.security.config;

import com.smart.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.security.filter.AuthCaptchaFilter;
import com.smart.module.api.auth.AuthCaptchaApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * 验证码 spring security配置类
 * @author ShiZhongMing
 * 2022/1/22
 * @since 2.0.0
 */
@Slf4j
public class AuthCaptchaSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H> {

    private AuthCaptchaSecurityConfigurer() {}

    public static <H extends HttpSecurityBuilder<H>> AuthCaptchaSecurityConfigurer<H> captcha() {
        return new AuthCaptchaSecurityConfigurer<>();
    }


    public H config(Customizer<AuthCaptchaSecurityConfigurer<H>> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void configure(H builder) {
        builder.addFilterBefore(new AuthCaptchaFilter(this.getBean(AuthProperties.class), this.getBean(AuthCaptchaApi.class)), BasicAuthenticationFilter.class);
    }

}
