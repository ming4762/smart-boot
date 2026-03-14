package com.smart.boot.autoconfigure.log;

import com.smart.framework.log.aspect.LogAspect;
import com.smart.framework.log.handler.DefaultLogHandler;
import com.smart.framework.log.handler.LogHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 日志配置类
 * @author jackson
 * 2020/1/22 1:52 下午
 */
@Configuration
@ConditionalOnClass(LogAspect.class)
@EnableConfigurationProperties(LogProperties.class)
public class SmartLogAutoConfiguration {

    /**
     * 创建日志切面
     * @return 日志切面
     */
    @Bean
    public LogAspect logAspect(LogProperties logProperties, List<LogHandler> logHandlerList) {
        return new LogAspect(logProperties.getConsole(), logProperties.getCodes(), logHandlerList);
    }

    @Bean
    @ConditionalOnMissingBean
    public LogHandler defaultLogHandler(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultLogHandler(applicationEventPublisher);
    }
}
