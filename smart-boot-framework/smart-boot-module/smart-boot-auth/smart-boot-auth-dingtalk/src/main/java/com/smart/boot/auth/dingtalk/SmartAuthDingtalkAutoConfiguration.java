package com.smart.boot.auth.dingtalk;

import com.smart.framework.auth.core.userdetails.UserDetailsBuilder;
import com.smart.framework.auth.extensions.dingtalk.SmartAuthDingtalkConfigurer;
import com.smart.framework.auth.extensions.dingtalk.userdetails.DefaultDingtalkUserDetailServiceImpl;
import com.smart.framework.auth.extensions.dingtalk.userdetails.DingtalkUserDetailService;
import com.smart.module.api.system.SystemAuthUserApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 钉钉认证扩展自动配置类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/7 16:07
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SmartAuthDingtalkConfigurer.class)
public class SmartAuthDingtalkAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DingtalkUserDetailService defaultDingtalkUserDetailService(SystemAuthUserApi systemAuthUserApi, UserDetailsBuilder userDetailsBuilder) {
        return new DefaultDingtalkUserDetailServiceImpl(systemAuthUserApi, userDetailsBuilder);
    }
}
