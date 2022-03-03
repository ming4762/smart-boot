package com.smart.monitor.actuator.logback.data;

import lombok.*;
import org.springframework.boot.logging.LogLevel;

import java.io.Serializable;
import java.util.List;

/**
 * 日志数据
 * @author ShiZhongMing
 * 2021/4/19 9:35
 * @since 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoggingEventData implements Serializable {
    private static final long serialVersionUID = -407583613061836652L;

    private String threadName;

    private String loggerName;

    private LogLevel level;

    private String message;

    private String formattedMessage;

    private Object[] argumentArray;



    private long timestamp;

    /**
     * 异常信息
     */
    private ThrowableData throwable;


    @Getter
    @Setter
    public static class ThrowableData {
        private String className;

        private String message;

        private List<StackTraceElement> stackTraceElementList;
    }
}
