package com.smart.monitor.actuator.logback.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import com.smart.monitor.actuator.logback.data.LoggingEventData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.logging.LogLevel;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/4/19 10:48
 * @since 1.0
 */
public class LogbackLogUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .withZone(ZoneId.systemDefault());

    private static final String POINT_SPIT = "\\.";

    /**
     * 转换日志内容
     * @param loggingEventData 日志数据
     * @return 日志内容
     */
    public static String convertToLogText(LoggingEventData loggingEventData) {
        final StringBuffer stringBuffer = new StringBuffer();
        // 添加时间戳
        stringBuffer.append( DATE_TIME_FORMATTER.format(Instant.ofEpochMilli(loggingEventData.getTimestamp())))
                .append(" ");
        // 添加日志级别
        stringBuffer.append(StringUtils.leftPad(loggingEventData.getLevel().name(), 5, " "))
                .append(" --- ")
                .append("[")
                .append(StringUtils.leftPad(convertThreadName(loggingEventData.getThreadName(), 15), 15, " "))
                .append("] ");
        // 添加LoggerName
        stringBuffer.append(StringUtils.rightPad(convertLoggerName(loggingEventData.getLoggerName(), 40), 40, " "))
                .append(" : ")
                .append(loggingEventData.getFormattedMessage());
        // 拼接异常信息
        final LoggingEventData.ThrowableData throwableData = loggingEventData.getThrowable();
        if (throwableData == null) {
            return stringBuffer.toString();
        }
        stringBuffer.append("\n")
                .append(throwableData.getClassName())
                .append(" : ")
                .append(throwableData.getMessage());
        // 拼接堆栈信息
        throwableData.getStackTraceElementList().forEach(item -> stringBuffer.append("\n")
                .append("    ")
                .append("at ")
                .append(item.getClassName())
                .append(".")
                .append(item.getMethodName())
                .append("(")
                .append(item.getFileName())
                .append(":")
                .append(item.getLineNumber())
                .append(")"));
        return stringBuffer.toString();
    }

    private static String convertThreadName(String threadName, int length) {
        if (threadName.length() <= length) {
            return threadName;
        }
        return threadName.substring(threadName.length() - length);
    }

    private static String convertLoggerName(String loggerName, int length) {
        if (loggerName.length() <= length) {
            return loggerName;
        }
        final String[] names = loggerName.split(POINT_SPIT);
        // 判断类名长度是否大于length
        if (names[names.length - 1].length() == length) {
            return names[names.length - 1];
        }
        if (names[names.length - 1].length() >= length) {
            return names[names.length - 1].substring(0, length);
        }
        // 将名字进行转换
        final List<String> nameList = new ArrayList<>(names.length);
        for (int i=0; i<names.length-1; i++) {
            nameList.add(names[i].substring(0, 1));
        }
        nameList.add(names[names.length - 1]);
        String result = String.join(".", nameList);
        if (result.length() > length) {
            result = result.substring((result.length() - length));
        }
        return result;
    }

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
