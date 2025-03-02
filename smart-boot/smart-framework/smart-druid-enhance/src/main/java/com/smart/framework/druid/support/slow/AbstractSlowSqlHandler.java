package com.smart.framework.druid.support.slow;

import com.alibaba.druid.proxy.jdbc.PreparedStatementProxyImpl;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /**
     * 慢SQL执行器
     *
     * @param statementProxy statementProxy
     * @param useTime        执行时间
     * @param parameter      参数
     */
    @Override
    public void handler(@NonNull StatementProxy statementProxy, Duration useTime, String parameter) {
        this.doHandler(this.createSlowSqlData(statementProxy, useTime, parameter));
    }

    /**
     * 慢SQL处理
     * @param slowSqlData 慢SQL数据
     */
    protected abstract void doHandler(@NonNull SlowSqlData slowSqlData);

    @NonNull
    protected SlowSqlData createSlowSqlData(@NonNull StatementProxy statementProxy, Duration useTime, String parameter) {
        return SlowSqlData.builder()
                .sqlId(statementProxy.getId())
                .dbType(statementProxy.getConnectionProxy().getDirectDataSource().getDbType())
                .sql(statementProxy.getLastExecuteSql())
                .parameter(parameter)
                .useTime(useTime)
                .columnList(this.getColumns(statementProxy))
                .datasourceName(statementProxy.getConnectionProxy().getDirectDataSource().getName())
                .timestamp(ZonedDateTime.now().toInstant().toEpochMilli())
                .build();
    }

    /**
     * 获取查询的列信息
     *
     * @param statementProxy statementProxy
     * @return 列信息
     */
    @SneakyThrows(SQLException.class)
    protected List<String> getColumns(StatementProxy statementProxy) {
        // 检查 statementProxy 是否为 PreparedStatementProxyImpl 类型
        // 如果不是，则无法获取元数据，直接返回空列表
        if (!(statementProxy instanceof PreparedStatementProxyImpl preparedStatement)) {
            return Collections.emptyList();
        }
        // 从 PreparedStatementProxyImpl 对象中获取结果集的元数据
        ResultSetMetaData metaData = preparedStatement.getMetaData();
        // 如果元数据为空，说明无法获取列信息，直接返回空列表
        if (metaData == null) {
            return Collections.emptyList();
        }

        // 创建一个 ArrayList 用于存储列信息，初始容量为结果集的列数
        List<String> columnList = new ArrayList<>(metaData.getColumnCount());
        // 遍历结果集中的每一列
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            // 将表名和列名以 "表名.列名" 的格式添加到 columnList 中
            columnList.add(metaData.getTableName(i) + "." + metaData.getColumnName(i));
        }
        // 返回包含所有列信息的列表
        return columnList;

    }
}
