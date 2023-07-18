package com.smart.message.manager;

import com.message.core.service.SmartSmsChannelService;
import com.message.core.service.SmartSmsService;
import com.smart.message.manager.service.SmartSmsChannelManagerService;
import com.smart.message.manager.service.SmartSmsLogService;
import com.smart.message.manager.service.impl.DefaultSmartSmsServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/5/26
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan
public class SmartMessageManagerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SmartSmsService.class)
    public SmartSmsService smartSmsService(SmartSmsChannelManagerService smartSmsChannelManagerService, List<SmartSmsChannelService> smartSmsChannelServiceList, SmartSmsLogService smartSmsLogService) {
        return new DefaultSmartSmsServiceImpl(smartSmsChannelManagerService, smartSmsChannelServiceList, smartSmsLogService);
    }
}
