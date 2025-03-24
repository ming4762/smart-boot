package com.smart.boot.actuate.autoconfigure.logback;

import com.smart.boot.actuate.logback.SmartMonitorActuatorLogback;
import com.smart.boot.actuate.logback.appender.MemoryCacheLogAppender;
import com.smart.boot.actuate.logback.point.LogbackEndPoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2022/3/2
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SmartMonitorActuatorLogback.class)
@ConditionalOnAvailableEndpoint(endpoint = LogbackEndPoint.class)
public class LogBackEndPointAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MemoryCacheLogAppender memoryCacheLogAppender() {
        return new MemoryCacheLogAppender();
    }

    @Bean
    @ConditionalOnMissingBean
    public LogbackEndPoint logbackEndPoint() {
        return new LogbackEndPoint();
    }
}
