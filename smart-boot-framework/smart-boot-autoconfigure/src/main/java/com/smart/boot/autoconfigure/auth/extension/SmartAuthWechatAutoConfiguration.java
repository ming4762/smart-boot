package com.smart.boot.autoconfigure.auth.extension;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.smart.framework.auth.extensions.wechat.SmartAuthWechatAppConfigurer;
import com.smart.framework.auth.extensions.wechat.provider.WechatAppLoginProvider;
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
@ConditionalOnClass(SmartAuthWechatAppConfigurer.class)
public class SmartAuthWechatAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WechatAppLoginProvider wechatAppLoginProvider(WxMaService wxMaService) {
        return new WechatAppLoginProvider(wxMaService);
    }
}
