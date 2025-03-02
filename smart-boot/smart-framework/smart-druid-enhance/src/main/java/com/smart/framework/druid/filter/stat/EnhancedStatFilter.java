package com.smart.framework.druid.filter.stat;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.druid.support.slow.SlowSqlHandler;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.time.*;
import java.util.*;

/**
 * 增强的StatFilter
 * 1、支持慢SQL处理
 * @author ShiZhongMing
 * 2021/4/2 9:26
 * @since 1.0
 */
public class EnhancedStatFilter extends StatFilter implements ApplicationContextAware {

    private static final Map<Class<?>, String> PARAMETER_STRING_TYPE_MAP = Map.of(
            InputStream.class, "<InputStream>",
            NClob.class, "<NClob>",
            Clob.class, "<Clob>",
            Blob.class, "<Blob>"
    );

    private static final List<Class<?>> PARAMETER_TYPE_LIST = List.of(
            String.class,
            Number.class,
            Boolean.class,
            Date.class,
            LocalDateTime.class,
            LocalDate.class,
            LocalTime.class,
            ZonedDateTime.class
    );

    /**
     * 存储慢SQL执行器
     */
    private final List<SlowSqlHandler> slowSqlHandlers = new LinkedList<>();

    @Override
    protected void handleSlowSql(StatementProxy statement) {
        // 获取执行时间
        final long nanos = System.nanoTime() - statement.getLastExecuteStartNano();
        final long millis = nanos / (1000 * 1000);
        final String parameter = this.buildSlowParameters(statement);
        this.slowSqlHandlers.forEach(item -> item.handler(statement, Duration.ofMillis(millis), parameter));
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.slowSqlHandlers.clear();
        Arrays.stream(applicationContext.getBeanNamesForType(SlowSqlHandler.class))
                .map(name -> applicationContext.getBean(name, SlowSqlHandler.class))
                .sorted(Comparator.comparingInt(Ordered::getOrder))
                .forEach(this.slowSqlHandlers::add);
    }

    @Override
    protected String buildSlowParameters(StatementProxy statement) {
        List<Object> parameters = new ArrayList<>(statement.getParametersSize());
        for (int i = 0; i < statement.getParametersSize(); i++) {
            JdbcParameter parameter = statement.getParameter(i);
            if (parameter == null) {
                continue;
            }
            Object value = parameter.getValue();
            if (value == null) {
                parameters.add("<Null>");
                continue;
            }
            Class<?> valueClass = value.getClass();
            if (PARAMETER_TYPE_LIST.contains(valueClass)) {
                parameters.add(value);
                continue;
            }
            if (PARAMETER_STRING_TYPE_MAP.containsKey(valueClass)) {
                parameters.add(PARAMETER_STRING_TYPE_MAP.get(valueClass));
                continue;
            }
            parameters.add("<" + value.getClass().getName() + ">");
        }
        return JsonUtils.toJsonString(parameters);
    }
}
