package com.smart.crud.spring;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.smart.crud.plus.tenant.DefaultTenantLineHandlerImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 租户自动配置功能
 * @author shizhongming
 * 2024/4/9 0:14
 * @since 3.0.0
 */
@Configuration
public class MybatisPlusTenantConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public TenantLineHandler tenantLineHandler() {
        return new DefaultTenantLineHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantLineHandler tenantLineHandler) {
        return new TenantLineInnerInterceptor(tenantLineHandler);
    }
}
