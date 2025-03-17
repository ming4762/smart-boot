package com.smart.boot.autoconfigure.crud;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.smart.boot.autoconfigure.crud.filter.SmartDataPermissionReactiveFilter;
import com.smart.boot.autoconfigure.crud.filter.SmartDataPermissionWebFilter;
import com.smart.framework.crud.datapermission.handler.SmartDataPermissionHandler;
import com.smart.framework.crud.datapermission.interceptor.SmartDataPermissionInterceptor;
import com.smart.module.api.crud.SmartCrudDataPermissionApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据权限自动配置类
 * @author shizhongming
 * 2025/3/5 20:52
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SmartDataPermissionAutoConfiguration {

    /**
     * 创建数据权限处理器
     * @return 数据权限处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public DataPermissionHandler dataPermissionHandler(SmartCrudDataPermissionApi smartCrudDataPermissionApi) {
        return new SmartDataPermissionHandler(smartCrudDataPermissionApi);
    }

    /**
     * 创建数据权限拦截器
     * @param dataPermissionHandler 数据权限处理器
     * @return 数据权限拦截器
     */
    @Bean
    @ConditionalOnMissingBean(DataPermissionInterceptor.class)
    public DataPermissionInterceptor dataPermissionInterceptor(DataPermissionHandler dataPermissionHandler) {
        return new SmartDataPermissionInterceptor(dataPermissionHandler);
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<SmartDataPermissionWebFilter> smartDataPermissionWebFilter() {
        FilterRegistrationBean<SmartDataPermissionWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SmartDataPermissionWebFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("smartDataPermissionWebFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public SmartDataPermissionReactiveFilter smartDataPermissionReactiveFilter() {
        return new SmartDataPermissionReactiveFilter();
    }
}
