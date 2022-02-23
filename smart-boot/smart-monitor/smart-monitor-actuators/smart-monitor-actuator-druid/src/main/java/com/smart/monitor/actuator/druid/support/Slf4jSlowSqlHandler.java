package com.smart.monitor.actuator.druid.support;

import com.alibaba.druid.proxy.jdbc.StatementProxy;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ShiZhongMing
 * 2021/4/2 11:17
 * @since 1.0
 */
@Slf4j
public class Slf4jSlowSqlHandler extends AbstractSlowSqlHandler {

    @Override
    public void handler(@NonNull StatementProxy statementProxy, long millis, String parameter) {
        log.warn("Slow Sql: {}", this.createSlowSqlData(statementProxy, millis, parameter));
    }
}
