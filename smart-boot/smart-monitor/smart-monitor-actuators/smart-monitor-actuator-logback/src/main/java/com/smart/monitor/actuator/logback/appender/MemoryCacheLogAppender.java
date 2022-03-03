package com.smart.monitor.actuator.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.smart.monitor.actuator.logback.data.LoggingEventData;
import com.smart.monitor.actuator.logback.utils.LogbackLogUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.boot.logging.LogLevel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * logback 日志内存存储器
 * @author ShiZhongMing
 * 2022/3/2
 * @since 2.0.0
 */
public class MemoryCacheLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    /**
     * 内存存储器 最多存储1万条日志
     */
    private static final Collection<LoggingEventData> CIRCULAR_FIFO_QUEUE = Collections.synchronizedCollection(new CircularFifoQueue<>(50000));

    @Override
    protected void append(ILoggingEvent loggingEvent) {
        CIRCULAR_FIFO_QUEUE.add(LogbackLogUtils.build(loggingEvent));
    }

    /**
     * 查询所有日志并清除
     * @return 日志
     */
    public static synchronized List<LoggingEventData> listAndClear() {
        final List<LoggingEventData> result = new ArrayList<>(CIRCULAR_FIFO_QUEUE);
        CIRCULAR_FIFO_QUEUE.clear();
        return result;
    }

    /**
     * 查询所有日志并清除
     * @return 日志
     */
    public static synchronized List<LoggingEventData> listAndClear(Set<LogLevel> levels) {
        final List<LoggingEventData> result = new ArrayList<>();
        final Iterator<LoggingEventData> iterable = CIRCULAR_FIFO_QUEUE.iterator();
        while (iterable.hasNext()) {
            final LoggingEventData event = iterable.next();
            if (levels.contains(event.getLevel())) {
                result.add(event);
                iterable.remove();
            }
        }
        return result;
    }

    /**
     * 查询所有日志
     * @return 日志
     */
    public static List<LoggingEventData> list() {
        return new ArrayList<>(CIRCULAR_FIFO_QUEUE);
    }

    /**
     * 查询日志列表
     * @param levels 日志级别
     * @return 日志列表
     */
    public static List<LoggingEventData> list(Set<LogLevel> levels) {
        if (levels.isEmpty()) {
            return new ArrayList<>(0);
        }
        return CIRCULAR_FIFO_QUEUE.stream().filter(item -> levels.contains(item.getLevel())).collect(Collectors.toList());
    }

}
