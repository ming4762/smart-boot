package com.smart.module.auth.config;

import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.core.tenant.filter.SmartAuthTenantInjectFilter;
import com.smart.module.api.auth.AuthApi;
import com.smart.module.auth.filter.SmartAuthTenantChangeFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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

    private final ServiceProvider serviceProvider = new ServiceProvider();

    private AuthTenantSecurityConfigurer() {}

    public static <H extends HttpSecurityBuilder<H>> AuthTenantSecurityConfigurer<H> tenant() {
        return new AuthTenantSecurityConfigurer<>();
    }


    public H config(Customizer<AuthTenantSecurityConfigurer<H>> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void init(H builder) {
        AuthenticationManagerBuilder authenticationManagerBuilder = builder.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.parentAuthenticationManager(null);
    }

    @Override
    public void configure(H builder) {
        SmartAuthTenantChangeFilter filter = new SmartAuthTenantChangeFilter(this.serviceProvider.tenantChangeUrl, this.getBean(AuthApi.class));
        filter.setAuthenticationManager(this.getBuilder().getSharedObject(AuthenticationManager.class));
        filter.setAuthenticationSuccessHandler(this.getBean(AuthenticationSuccessHandler.class));
        // 设置登录失败handler
        filter.setAuthenticationFailureHandler(this.getBean(AuthenticationFailureHandler.class));
        filter.setSecurityContextRepository(builder.getSharedObject(SecurityContextRepository.class));

        builder.addFilterAfter(new SmartAuthTenantInjectFilter(), SecurityContextHolderFilter.class)
                .addFilterAfter(filter, ExceptionTranslationFilter.class);
    }

    public AuthTenantSecurityConfigurer<H> url(String url) {
        this.serviceProvider.tenantChangeUrl = url;
        return this;
    }

    private static class ServiceProvider {
        private String tenantChangeUrl;

        private ServiceProvider() {
            this.tenantChangeUrl = "/auth/tenant/change";
        }
    }

}
