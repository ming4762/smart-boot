package com.smart.monitor.actuator.druid.filter;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.smart.monitor.actuator.druid.support.SlowSqlHandler;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 增强的StatFilter
 * 1、支持慢SQL处理
 * @author ShiZhongMing
 * 2021/4/2 9:26
 * @since 1.0
 */
public class EnhancedStatFilter extends StatFilter implements ApplicationContextAware {

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
        this.slowSqlHandlers.forEach(item -> item.handler(statement, millis, parameter));
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        // 初始化慢SQL执行器
        this.slowSqlHandlers.clear();
        Arrays.stream(applicationContext.getBeanNamesForType(SlowSqlHandler.class))
                .map(name -> applicationContext.getBean(name, SlowSqlHandler.class))
                .sorted(Comparator.comparingInt(Ordered::getOrder))
                .forEach(this.slowSqlHandlers::add);
    }
}
