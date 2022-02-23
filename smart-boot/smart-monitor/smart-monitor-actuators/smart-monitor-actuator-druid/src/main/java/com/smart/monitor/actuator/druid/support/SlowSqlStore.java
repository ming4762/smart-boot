package com.smart.monitor.actuator.druid.support;

import com.alibaba.druid.proxy.jdbc.StatementProxy;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 慢SQL 存储
 * @author ShiZhongMing
 * 2021/4/7 8:58
 * @since 1.0
 */
public interface SlowSqlStore {

    /**
     * 保存慢SQL
     * @param statementProxy statementProxy
     * @param millis 执行毫秒数
     * @param parameter 参数
     */
    void save(@NonNull StatementProxy statementProxy, long millis, String parameter);

    /**
     * 查询慢SQL列表
     * @param clear 是否清除
     * @return 慢SQL列表
     */
    @NonNull
    List<SlowSqlData> list(boolean clear);

    /**
     * 通过数据库名字查询慢SQL列表
     * @param datasourceName 数据源名字
     * @param clear 是否清除
     * @return 慢SQL列表
     */
    @NonNull
    List<SlowSqlData> listByDatasourceName(@NonNull String datasourceName, boolean clear);
}
