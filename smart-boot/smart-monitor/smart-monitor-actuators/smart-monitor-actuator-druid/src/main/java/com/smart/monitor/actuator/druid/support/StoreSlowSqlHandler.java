package com.smart.monitor.actuator.druid.support;

import com.alibaba.druid.proxy.jdbc.StatementProxy;
import lombok.NonNull;

/**
 * 慢SQL存储handler
 * @author ShiZhongMing
 * 2021/4/7 9:20
 * @since 1.0
 */
public class StoreSlowSqlHandler implements SlowSqlHandler {

    private final SlowSqlStore slowSqlStore;

    public StoreSlowSqlHandler(SlowSqlStore slowSqlStore) {
        this.slowSqlStore = slowSqlStore;
    }

    @Override
    public void handler(@NonNull StatementProxy statementProxy, long millis, String parameter) {
        this.slowSqlStore.save(statementProxy, millis, parameter);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
