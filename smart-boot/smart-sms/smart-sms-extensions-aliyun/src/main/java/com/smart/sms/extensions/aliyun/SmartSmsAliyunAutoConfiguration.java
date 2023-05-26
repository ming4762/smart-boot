package com.smart.sms.extensions.aliyun;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/5/26
 */
@Configuration(proxyBeanMethods = false)
public class SmartSmsAliyunAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmartAliyunSmsChannelService aliyunSmartSmsChannelService() {
        return new SmartAliyunSmsChannelServiceImpl();
    }
}
