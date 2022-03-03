package com.smart.monitor.autoconfigure.actuator.logback;

import com.smart.monitor.actuator.logback.SmartMonitorActuatorLogback;
import com.smart.monitor.actuator.logback.appender.MemoryCacheLogAppender;
import com.smart.monitor.actuator.logback.point.LogbackEndPoint;
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
