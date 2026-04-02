package com.smart.boot.message.websocket;

import com.smart.framework.extension.dingtalk.DingtalkApi;
import com.smart.framework.message.dingtalk.sender.SmartDingtalkWorkNoticeSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/5/26
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SmartDingtalkWorkNoticeSender.class)
public class SmartMessageDingtalkAutoConfiguration {

    /**
     * 钉钉工作通知
     * @return SmartDingdingWorkNoticeSender
     */
    @Bean
    public SmartDingtalkWorkNoticeSender dingdingWorkNoticeSender(DingtalkApi dingtalkApi) {
        return new SmartDingtalkWorkNoticeSender(dingtalkApi);
    }
}
