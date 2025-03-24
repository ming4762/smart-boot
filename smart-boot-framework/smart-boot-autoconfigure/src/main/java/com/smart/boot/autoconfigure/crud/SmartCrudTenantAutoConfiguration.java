package com.smart.boot.autoconfigure.crud;

import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.smart.boot.autoconfigure.crud.filter.SmartTenantReactiveFilter;
import com.smart.boot.autoconfigure.crud.filter.SmartTenantWebFilter;
import com.smart.framework.crud.service.BaseService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 租户自动配置类
 * @author shizhongming
 * 2025/2/13 14:05
 * @since 5.0.0
 */
@Configuration
@ConditionalOnBean(TenantLineInnerInterceptor.class)
@ConditionalOnClass(BaseService.class)
public class SmartCrudTenantAutoConfiguration {


    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<SmartTenantWebFilter> smartTenantWebFilter() {
        FilterRegistrationBean<SmartTenantWebFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SmartTenantWebFilter());
        registration.addUrlPatterns("/*");
        registration.setName("smartTenantWebFilter");
        registration.setOrder(Integer.MIN_VALUE);
        return registration;
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public SmartTenantReactiveFilter smartTenantReactiveFilter() {
        return new SmartTenantReactiveFilter();
    }
}
