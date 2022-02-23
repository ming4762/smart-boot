package com.smart.monitor.actuator.druid.support;

import com.alibaba.druid.proxy.jdbc.StatementProxy;
import lombok.NonNull;

/**
 * @author ShiZhongMing
 * 2021/4/2 11:11
 * @since 1.0
 */
public abstract class AbstractSlowSqlHandler implements SlowSqlHandler {


    @Override
    public int getOrder() {
        return 0;
    }

    @NonNull
    protected SlowSqlData createSlowSqlData(@NonNull StatementProxy statementProxy, long millis, String parameter) {
        return SlowSqlData.builder()
                .sql(statementProxy.getLastExecuteSql())
                .parameter(parameter)
                .useMillis(millis)
                .datasourceName(statementProxy.getConnectionProxy().getDirectDataSource().getName())
                .build();
    }
}
