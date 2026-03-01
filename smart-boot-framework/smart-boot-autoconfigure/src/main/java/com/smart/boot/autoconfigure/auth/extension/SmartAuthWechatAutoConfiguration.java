package com.smart.boot.autoconfigure.auth.extension;

import com.smart.boot.autoconfigure.wechat.SmartWechatMpAutoconfiguration;
import com.smart.framework.auth.core.userdetails.UserDetailsBuilder;
import com.smart.framework.auth.extensions.wechat.AuthWechatSecurityConfigurer;
import com.smart.framework.auth.extensions.wechat.userdetails.DefaultWechatUserDetailServiceImpl;
import com.smart.framework.auth.extensions.wechat.userdetails.WechatUserDetailService;
import com.smart.module.api.system.SystemAuthUserApi;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2024/9/11 16:06
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AuthWechatSecurityConfigurer.class)
@AutoConfigureAfter(SmartWechatMpAutoconfiguration.class)
public class SmartAuthWechatAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WechatUserDetailService defaultWechatUserDetailService(SystemAuthUserApi systemAuthUserApi, UserDetailsBuilder userDetailsBuilder) {
        return new DefaultWechatUserDetailServiceImpl(systemAuthUserApi, userDetailsBuilder);
    }
}
