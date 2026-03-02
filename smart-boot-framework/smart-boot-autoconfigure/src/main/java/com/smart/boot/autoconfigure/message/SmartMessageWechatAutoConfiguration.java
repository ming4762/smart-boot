package com.smart.boot.autoconfigure.message;

import com.smart.boot.autoconfigure.wechat.SmartWechatMpAutoconfiguration;
import com.smart.framework.message.wechat.mq.WechatMessageEventMqProducer;
import com.smart.framework.message.wechat.receive.WechatMapMessageReceiveController;
import com.smart.framework.rocketmq.producer.SmartMqProducer;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信消息自动配置类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-28 15:00
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(SmartWechatMpAutoconfiguration.class)
public class SmartMessageWechatAutoConfiguration {

    @Bean
    public WechatMapMessageReceiveController wechatMapMessageReceiveController(WxMpService wxMpService) {
        return new WechatMapMessageReceiveController(wxMpService);
    }

    @Bean
    @ConditionalOnBean(SmartMqProducer.class)
    public WechatMessageEventMqProducer wechatMessageEventMqProducer(SmartMqProducer smartMqProducer) {
        return new WechatMessageEventMqProducer(smartMqProducer);
    }
}
