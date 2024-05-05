package com.smart.message.dingding;

import com.smart.message.dingding.sender.SmartDingdingWorkNoticeSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/5/26
 */
@Configuration(proxyBeanMethods = false)
public class SmartMessageDingdingAutoConfiguration {

    /**
     * 钉钉工作通知
     * @return SmartDingdingWorkNoticeSender
     */
    @Bean
    public SmartDingdingWorkNoticeSender dingdingWorkNoticeSender() {
        return new SmartDingdingWorkNoticeSender();
    }
}
