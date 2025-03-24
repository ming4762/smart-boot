package com.smart.framework.crud.datapermission.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;

/**
 * 数据权限拦截器
 * 增强mybatis plus 数据权限拦截器
 * @author shizhongming
 * 2025/3/5 20:56
 * @since 5.0.0
 */
public class SmartDataPermissionInterceptor extends DataPermissionInterceptor {

    public SmartDataPermissionInterceptor(DataPermissionHandler dataPermissionHandler) {
        super(dataPermissionHandler);
    }

    /**
     * 重写函数
     * 1、增强忽略逻辑，支持函数配置
     * @param executor      Executor(可能是代理对象)
     * @param ms            MappedStatement
     * @param parameter     parameter
     * @param rowBounds     rowBounds
     * @param resultHandler resultHandler
     * @param boundSql      boundSql
     * @throws SQLException SQLException
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }
}
