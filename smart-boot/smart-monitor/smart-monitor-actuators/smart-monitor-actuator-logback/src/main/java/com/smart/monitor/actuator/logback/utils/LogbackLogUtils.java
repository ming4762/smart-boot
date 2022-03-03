package com.smart.monitor.actuator.logback.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import com.smart.monitor.actuator.logback.data.LoggingEventData;
import org.springframework.boot.logging.LogLevel;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/4/19 10:48
 * @since 1.0
 */
public class LogbackLogUtils {

    /**
     * 构建LoggingEventData
     * @param loggingEvent Logback日志信息
     * @return LoggingEventData
     */
    public static LoggingEventData build(ILoggingEvent loggingEvent) {
        LoggingEventData.ThrowableData throwableData = null;
        final IThrowableProxy throwableProxy = loggingEvent.getThrowableProxy();
        if (throwableProxy != null) {
            throwableData = new LoggingEventData.ThrowableData();
            throwableData.setClassName(throwableProxy.getClassName());
            throwableData.setMessage(throwableProxy.getMessage());
            if (throwableProxy.getStackTraceElementProxyArray() != null) {
                throwableData.setStackTraceElementList(
                        Arrays.stream(throwableProxy.getStackTraceElementProxyArray())
                        .map(StackTraceElementProxy::getStackTraceElement)
                        .collect(Collectors.toList())
                );
            }
        }
        final LoggingEventData loggingEventData = LoggingEventData.builder()
                .threadName(loggingEvent.getThreadName())
                .loggerName(loggingEvent.getLoggerName())
                .level(LogLevel.valueOf(loggingEvent.getLevel().levelStr))
                .message(loggingEvent.getMessage())
                .formattedMessage(loggingEvent.getFormattedMessage())
                .timestamp(loggingEvent.getTimeStamp())
                .throwable(throwableData)
                .build();
        if (loggingEvent.getArgumentArray() != null) {
            String[] argumentArray = new String[loggingEvent.getArgumentArray().length];
            for (int i=0; i<loggingEvent.getArgumentArray().length; i++) {
               final Object obj = loggingEvent.getArgumentArray()[i];
               if (obj != null) {
                   argumentArray[i] = obj.toString();
               } else {
                   argumentArray[i] = null;
               }
            }
            loggingEventData.setArgumentArray(argumentArray);
        }
        return loggingEventData;
    }
}
