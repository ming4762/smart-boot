package com.smart.monitor.actuator.logback.listener;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.smart.monitor.actuator.logback.appender.MemoryCacheLogAppender;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

/**
 * @author ShiZhongMing
 * 2022/3/3
 * @since 2.0.0
 */
public class LogAppenderInitListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(@NonNull ApplicationEnvironmentPreparedEvent event) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        MemoryCacheLogAppender logAppender = new MemoryCacheLogAppender();
        logAppender.start();
        logger.addAppender(logAppender);
    }
}
