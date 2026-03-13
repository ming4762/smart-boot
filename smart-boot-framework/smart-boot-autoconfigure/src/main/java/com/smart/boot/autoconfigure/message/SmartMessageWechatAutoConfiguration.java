package com.smart.boot.autoconfigure.message;

import com.smart.boot.autoconfigure.wechat.SmartWechatMpAutoconfiguration;
import com.smart.framework.extension.wechat.config.SmartWechatConfigStorageCreator;
import com.smart.framework.message.wechat.receive.WechatMpMessageReceiveController;
import com.smart.framework.message.wechat.sender.WechatMpTemplateMessageSender;
import com.smart.framework.rocketmq.producer.SmartMqProducer;
import com.smart.module.api.system.SysUserApi;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@ConditionalOnClass(WechatMpTemplateMessageSender.class)
public class SmartMessageWechatAutoConfiguration {

    @Bean
    public WechatMpMessageReceiveController wechatMapMessageReceiveController(WxMpService wxMpService) {
        return new WechatMpMessageReceiveController(wxMpService);
    }

    @Bean
    @ConditionalOnMissingBean(WechatMpTemplateMessageSender.class)
    public WechatMpTemplateMessageSender wechatMpTemplateMessageSender(
            WxMpService wxMpService,
            SmartWechatConfigStorageCreator smartWechatConfigStorageCreator,
            SysUserApi sysUserApi
    ) {
        return new WechatMpTemplateMessageSender(wxMpService, smartWechatConfigStorageCreator, sysUserApi);
    }
}
