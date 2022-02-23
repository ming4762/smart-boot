package com.smart.monitor.actuator.druid.constants;

import lombok.Getter;

/**
 * 数据库连接池指标
 * @author ShiZhongMing
 * 2021/4/2 14:26
 * @since 1.0
 */
@Getter
public enum DatasourceMetricsConstants {
    /**
     * 池中连接数
     */
    poolingCount("druid.datasource.poolingCount", "PoolingCount", "当前连接池中的数目"),
    /**
     * 池中连接数峰值
     */
    poolingPeak("druid.datasource.poolingPeak", "PoolingPeak", "连接池中数目的峰值"),
    /**
     * 活跃连接数
     */
    activeCount("druid.datasource.activeCount", "ActiveCount", "当前连接池中活跃连接数"),
    /**
     * 活跃连接数峰值
     */
    activePeak("druid.datasource.activePeak", "ActivePeak", "连接池中活跃连接数峰值"),
    /**
     * 逻辑连接打开次数
     */
    logicConnectCount("druid.datasource.logicConnectCount", "LogicConnectCount", "产生的逻辑连接建立总数"),
    /**
     * 逻辑连接关闭次数
     */
    logicCloseCount("druid.datasource.logicCloseCount", "LogicCloseCount", "产生的逻辑连接关闭总数"),
    /**
     * 逻辑连接错误次数
     */
    logicConnectErrorCount("druid.datasource.logicConnectErrorCount", "LogicConnectErrorCount", "产生的逻辑连接出错总数"),
    /**
     * 执行数
     */
    executeCount("druid.datasource.executeCount", "ExecuteCount", "数据库执行次数"),
    /**
     * 提交数
     */
    commitCount("druid.datasource.commitCount", "CommitCount", "事务提交次数"),
    /**
     * 错误数
     */
    errorCount("druid.datasource.errorCount", "ErrorCount", "数据库执行错误次数"),
    /**
     * 回滚数
     */
    rollbackCount("druid.datasource.rollbackCount", "RollbackCount", "事务回滚次数"),
    /**
     * 真实PreparedStatement打开次数
     */
    preparedStatementOpenCount("druid.datasource.preparedStatementOpenCount", "PreparedStatementOpenCount", "真实PreparedStatement打开次数"),
    /**
     * 真实PreparedStatement关闭次数
     */
    preparedStatementClosedCount("druid.datasource.preparedStatementClosedCount", "PreparedStatementClosedCount", "真实PreparedStatement关闭次数"),
    /**
     * 获取连接时累计等待多少次
     */
    notEmptyWaitCount("druid.datasource.notEmptyWaitCount", "NotEmptyWaitCount", "获取连接时累计等待多少次"),
    /**
     * 获取连接时累计等待多长时间
     */
    notEmptyWaitMillis("druid.datasource.notEmptyWaitMillis", "NotEmptyWaitMillis", "获取连接时累计等待多长时间"),
    /**
     * 当前等待获取连接的线程数
     */
    waitThreadCount("druid.datasource.waitThreadCount", "WaitThreadCount", "当前等待获取连接的线程数"),
    ;

    private final String meterName;

    private final String property;

    private final String description;

    DatasourceMetricsConstants(String meterName, String property, String description) {
        this.meterName = meterName;
        this.property = property;
        this.description = description;
    }

}
