package com.smart.auth.autoconfigure.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.smart.auth.extensions.wechat.AuthWechatConfigure;
import com.smart.auth.extensions.wechat.provider.WechatAppLoginProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/4/4
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AuthWechatConfigure.class)
public class AuthWechatAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WechatAppLoginProvider wechatAppLoginProvider(WxMaService wxMaService) {
        return new WechatAppLoginProvider(wxMaService);
    }
}
