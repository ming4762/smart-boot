package com.smart.cloud.starter.exception;

import com.smart.cloud.starter.exception.notice.RemoteExceptionNotice;
import com.smart.module.api.system.SysExceptionApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/3/12
 */
@Configuration(proxyBeanMethods = false)
public class SmartCloudExceptionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "dbExceptionNotice")
    public RemoteExceptionNotice remoteExceptionNotice(SysExceptionApi sysExceptionApi) {
        return new RemoteExceptionNotice(sysExceptionApi);
    }
}
