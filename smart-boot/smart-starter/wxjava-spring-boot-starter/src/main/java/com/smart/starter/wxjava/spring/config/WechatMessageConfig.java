package com.smart.starter.wxjava.spring.config;

import com.smart.starter.wxjava.message.WechatMessageController;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/4/7
 */
@Configuration(proxyBeanMethods = false)
public class WechatMessageConfig {

    @Bean
    public WechatMessageController wechatMessageController(ApplicationEventPublisher applicationEventPublisher, WxMpService wxMpService) {
        return new WechatMessageController(applicationEventPublisher, wxMpService);
    }
}
