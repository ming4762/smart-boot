package com.smart.starter.log;

import com.smart.starter.log.aspect.LogAspect;
import com.smart.starter.log.handler.DefaultLogHandler;
import com.smart.starter.log.handler.LogHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志配置类
 * @author jackson
 * 2020/1/22 1:52 下午
 */
@Configuration
@EnableConfigurationProperties(LogProperties.class)
public class SmartLogAutoConfiguration {

    /**
     * 创建日志切面
     * @return 日志切面
     */
    @Bean
    public LogAspect logAspect(LogProperties logProperties, LogHandler logHandler) {
        return new LogAspect(logProperties, logHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public LogHandler defaultLogHandler() {
        return new DefaultLogHandler();
    }
}
