package com.smart.boot.autoconfigure.message;

import com.smart.framework.message.sms.aliyun.SmartAliyunSmsChannelService;
import com.smart.framework.message.sms.aliyun.SmartAliyunSmsChannelServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/5/26
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SmartAliyunSmsChannelService.class})
public class SmartSmsAliyunAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmartAliyunSmsChannelService aliyunSmartSmsChannelService() {
        return new SmartAliyunSmsChannelServiceImpl();
    }
}
