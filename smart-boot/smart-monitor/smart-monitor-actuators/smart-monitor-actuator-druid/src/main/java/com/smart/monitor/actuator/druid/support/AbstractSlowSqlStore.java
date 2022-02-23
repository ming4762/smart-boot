package com.smart.monitor.actuator.druid.support;

import com.alibaba.druid.proxy.jdbc.StatementProxy;
import lombok.NonNull;

/**
 * @author ShiZhongMing
 * 2021/4/7 9:02
 * @since 1.0
 */
public abstract class AbstractSlowSqlStore implements SlowSqlStore {

    @NonNull
    protected SlowSqlData createSlowSqlData(@NonNull StatementProxy statementProxy, long millis, String parameter) {
        return SlowSqlData.builder()
                .id(statementProxy.getSqlStat().getId())
                .sql(statementProxy.getLastExecuteSql())
                .parameter(parameter)
                .timestamp(System.currentTimeMillis())
                .useMillis(millis)
                .datasourceName(statementProxy.getConnectionProxy().getDirectDataSource().getName())
                .build();
    }
}
