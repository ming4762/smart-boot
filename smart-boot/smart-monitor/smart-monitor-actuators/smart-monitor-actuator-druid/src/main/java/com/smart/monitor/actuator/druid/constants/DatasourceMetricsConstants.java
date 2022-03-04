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
    POOLING_COUNT("druid.datasource.poolingCount", "PoolingCount", "当前连接池中的数目"),
    /**
     * 池中连接数峰值
     */
    POOLING_PEAK("druid.datasource.poolingPeak", "PoolingPeak", "连接池中数目的峰值"),
    /**
     * 活跃连接数
     */
    ACTIVE_COUNT("druid.datasource.activeCount", "ActiveCount", "当前连接池中活跃连接数"),
    /**
     * 活跃连接数峰值
     */
    ACTIVE_PEAK("druid.datasource.activePeak", "ActivePeak", "连接池中活跃连接数峰值"),
    /**
     * 逻辑连接打开次数
     */
    LOGIC_CONNECT_COUNT("druid.datasource.logicConnectCount", "LogicConnectCount", "产生的逻辑连接建立总数"),
    /**
     * 逻辑连接关闭次数
     */
    LOGIC_CLOSE_COUNT("druid.datasource.logicCloseCount", "LogicCloseCount", "产生的逻辑连接关闭总数"),
    /**
     * 逻辑连接错误次数
     */
    LOGIC_CONNECT_ERROR_COUNT("druid.datasource.logicConnectErrorCount", "LogicConnectErrorCount", "产生的逻辑连接出错总数"),
    /**
     * 执行数
     */
    EXECUTE_COUNT("druid.datasource.executeCount", "ExecuteCount", "数据库执行次数"),
    /**
     * 提交数
     */
    COMMIT_COUNT("druid.datasource.commitCount", "CommitCount", "事务提交次数"),
    /**
     * 错误数
     */
    ERROR_COUNT("druid.datasource.errorCount", "ErrorCount", "数据库执行错误次数"),
    /**
     * 回滚数
     */
    ROLLBACK_COUNT("druid.datasource.rollbackCount", "RollbackCount", "事务回滚次数"),
    /**
     * 真实PreparedStatement打开次数
     */
    PREPARED_STATEMENT_OPEN_COUNT("druid.datasource.preparedStatementOpenCount", "PreparedStatementOpenCount", "真实PreparedStatement打开次数"),
    /**
     * 真实PreparedStatement关闭次数
     */
    PREPARED_STATEMENT_CLOSED_COUNT("druid.datasource.preparedStatementClosedCount", "PreparedStatementClosedCount", "真实PreparedStatement关闭次数"),
    /**
     * 获取连接时累计等待多少次
     */
    NOT_EMPTY_WAIT_COUNT("druid.datasource.notEmptyWaitCount", "NotEmptyWaitCount", "获取连接时累计等待多少次"),
    /**
     * 获取连接时累计等待多长时间
     */
    NOT_EMPTY_WAIT_MILLIS("druid.datasource.notEmptyWaitMillis", "NotEmptyWaitMillis", "获取连接时累计等待多长时间"),
    /**
     * 当前等待获取连接的线程数
     */
    WAIT_THREAD_COUNT("druid.datasource.waitThreadCount", "WaitThreadCount", "当前等待获取连接的线程数"),
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
