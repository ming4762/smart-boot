package com.smart.boot.autoconfigure.auth.extension;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.smart.boot.autoconfigure.wechat.SmartWechatMpAutoconfiguration;
import com.smart.framework.auth.core.userdetails.UserDetailsBuilder;
import com.smart.framework.auth.core.wechat.WechatAuthConfigProvider;
import com.smart.framework.auth.extensions.wechat.AuthWechatAppConfigurer;
import com.smart.framework.auth.extensions.wechat.provider.DefaultWechatMpQrcodeCreateProviderImpl;
import com.smart.framework.auth.extensions.wechat.provider.WechatAppLoginProvider;
import com.smart.framework.auth.extensions.wechat.provider.WechatMpQrcodeCreateProvider;
import com.smart.framework.auth.extensions.wechat.userdetails.DefaultWechatUserDetailServiceImpl;
import com.smart.framework.auth.extensions.wechat.userdetails.WechatUserDetailService;
import com.smart.module.api.system.SystemAuthUserApi;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@ConditionalOnClass(AuthWechatAppConfigurer.class)
@AutoConfigureAfter(SmartWechatMpAutoconfiguration.class)
public class SmartAuthWechatAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WechatAppLoginProvider wechatAppLoginProvider(WxMaService wxMaService) {
        return new WechatAppLoginProvider(wxMaService);
    }

    @Bean
    @ConditionalOnMissingBean
    public WechatUserDetailService defaultWchatUserDetailService(SystemAuthUserApi systemAuthUserApi, UserDetailsBuilder userDetailsBuilder) {
        return new DefaultWechatUserDetailServiceImpl(systemAuthUserApi, userDetailsBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(WxMpService.class)
    public WechatMpQrcodeCreateProvider defaultWechatMpQrcodeCreateProvider(WxMpService wxMpService, ObjectProvider<WechatAuthConfigProvider> wechatAuthConfigProvider) {
        return new DefaultWechatMpQrcodeCreateProviderImpl(wxMpService, wechatAuthConfigProvider.getIfAvailable());
    }
}
