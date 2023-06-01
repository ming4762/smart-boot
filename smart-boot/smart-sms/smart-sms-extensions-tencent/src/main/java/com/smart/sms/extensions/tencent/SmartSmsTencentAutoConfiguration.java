package com.smart.sms.extensions.tencent;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/5/26
 */
@Configuration(proxyBeanMethods = false)
public class SmartSmsTencentAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmartTencentSmsChannelService  smartTencentSmsChannelService() {
        return new SmartTencentSmsChannelServiceImpl();
    }
}