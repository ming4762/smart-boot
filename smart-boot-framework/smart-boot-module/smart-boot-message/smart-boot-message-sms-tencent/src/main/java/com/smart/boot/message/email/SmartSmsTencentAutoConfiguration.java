package com.smart.boot.message.email;

import com.smart.framework.message.sms.tencent.SmartTencentSmsChannelService;
import com.smart.framework.message.sms.tencent.SmartTencentSmsChannelServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/5/26
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SmartTencentSmsChannelService.class)
public class SmartSmsTencentAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmartTencentSmsChannelService smartTencentSmsChannelService() {
        return new SmartTencentSmsChannelServiceImpl();
    }
}
