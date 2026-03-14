package com.smart.boot.autoconfigure.auth.extension;

import com.smart.boot.autoconfigure.mq.rocket.SmartMqAutoconfiguration;
import com.smart.boot.autoconfigure.wechat.SmartWechatMpAutoconfiguration;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.core.userdetails.UserDetailsBuilder;
import com.smart.framework.auth.core.wechat.WechatAuthConfigProvider;
import com.smart.framework.auth.extensions.wechat.AuthWechatSecurityConfigurer;
import com.smart.framework.auth.extensions.wechat.listener.WechatLoginScanSpringEventListener;
import com.smart.framework.auth.extensions.wechat.provider.DefaultWechatAuthConfigProviderImpl;
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
@AutoConfigureAfter({ SmartWechatMpAutoconfiguration.class, SmartMqAutoconfiguration.class })
public class SmartAuthWechatAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WechatUserDetailService defaultWechatUserDetailService(SystemAuthUserApi systemAuthUserApi, UserDetailsBuilder userDetailsBuilder) {
        return new DefaultWechatUserDetailServiceImpl(systemAuthUserApi, userDetailsBuilder);
    }

    /**
     * 微信登录扫码Spring事件监控器
     * @return 微信登录扫码Spring事件监控器
     */
    @Bean
    @ConditionalOnClass(WechatLoginScanSpringEventListener.class)
    public WechatLoginScanSpringEventListener wechatLoginScanSpringEventMonitor(AuthCache authCache) {
        return new WechatLoginScanSpringEventListener(authCache);
    }


    /**
     * 创建微信授权配置提供者
     * @return 微信授权配置提供者
     */
    @Bean
    @ConditionalOnMissingBean
    public WechatAuthConfigProvider wechatAuthConfigProvider() {
        return new DefaultWechatAuthConfigProviderImpl();
    }
}
