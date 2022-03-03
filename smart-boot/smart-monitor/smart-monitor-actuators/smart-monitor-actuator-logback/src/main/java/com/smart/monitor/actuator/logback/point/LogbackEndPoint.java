package com.smart.monitor.actuator.logback.point;

import com.smart.monitor.actuator.logback.appender.MemoryCacheLogAppender;
import com.smart.monitor.actuator.logback.data.LoggingEventData;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.logging.LogLevel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/4/13 17:02
 * @since 1.0
 */
@Endpoint(id = "logback")
public class LogbackEndPoint {

    @ReadOperation
    public List<LoggingEventData> list() {
        return MemoryCacheLogAppender.list();
    }

    @WriteOperation
    public List<LoggingEventData> listClear() {
        return MemoryCacheLogAppender.listAndClear();
    }

    @WriteOperation
    public List<LoggingEventData> listClearWithLevel(@Selector String levels) {
        return MemoryCacheLogAppender.listAndClear(
                Arrays.stream(levels.split(","))
                        .map(item -> LogLevel.valueOf(item.trim()))
                        .collect(Collectors.toSet())
        );
    }

    /**
     * 查询指定级别的日志
     * @param levels 日志级别
     * @return 日志列表
     */
    @ReadOperation
    public List<LoggingEventData> listWithLevel(@Selector String levels) {
        return MemoryCacheLogAppender.list(
                Arrays.stream(levels.split(","))
                        .map(item -> LogLevel.valueOf(item.trim()))
                        .collect(Collectors.toSet())
        );
    }
}
