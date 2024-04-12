package com.smart.auth.security.config;

import com.smart.auth.core.authentication.SmartAuthenticationEventPublisher;
import com.smart.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.auth.core.handler.AuthSuccessDataHandler;
import com.smart.auth.core.userdetails.UserDetailsBuilder;
import com.smart.auth.security.filter.SmartAuthTenantChangeFilter;
import com.smart.auth.security.tenant.SmartAuthTenantInjectFilter;
import com.smart.module.api.auth.AuthApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * 验证码 spring security配置类
 * @author ShiZhongMing
 * 2022/1/22
 * @since 2.0.0
 */
@Slf4j
public class AuthTenantSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H> {

    private AuthTenantSecurityConfigurer() {}

    public static <H extends HttpSecurityBuilder<H>> AuthTenantSecurityConfigurer<H> tenant() {
        return new AuthTenantSecurityConfigurer<>();
    }


    public H config(Customizer<AuthTenantSecurityConfigurer<H>> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void configure(H builder) {
        SmartAuthTenantChangeFilter filter = new SmartAuthTenantChangeFilter(
                this.getBean(UserDetailsBuilder.class),
                this.getBean(AuthApi.class),
                this.getBean(SecurityContextRepository.class),
                this.getBean(AuthSuccessDataHandler.class),
                this.getBean(SmartAuthenticationEventPublisher.class)
        );
        builder.addFilterAfter(new SmartAuthTenantInjectFilter(), SecurityContextHolderFilter.class)
                .addFilterAfter(filter, ExceptionTranslationFilter.class);
    }

}
