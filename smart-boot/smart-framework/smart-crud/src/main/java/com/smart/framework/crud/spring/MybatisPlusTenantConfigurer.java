package com.smart.framework.crud.spring;

import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.smart.framework.crud.plus.handlers.SmartTenantLineHandler;
import com.smart.framework.crud.plus.inner.SmartTenantLineInnerInterceptor;
import com.smart.framework.crud.plus.tenant.DefaultTenantLineHandlerImpl;
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
    public SmartTenantLineHandler tenantLineHandler() {
        return new DefaultTenantLineHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(SmartTenantLineHandler smartTenantLineHandler) {
        return new SmartTenantLineInnerInterceptor(smartTenantLineHandler);
    }
}
