package com.smart.crud.datapermission;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * 数据权限拦截器
 * @author ShiZhongMing
 * 2022/10/17
 * @since 3.0.0
 */
@Intercepts(
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        )
)
public class DataPermissionExecutorInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws SQLException, InvocationTargetException, IllegalAccessException {
        var permissionSql = DataPermissionHolder.getSql();
        if (StringUtils.isBlank(permissionSql)) {
            return invocation.proceed();
        }

        var executor = (Executor) invocation.getTarget();
        var ms = (MappedStatement) invocation.getArgs()[0];
        var parameter = invocation.getArgs()[1];
        var boundSql = ms.getBoundSql(parameter);
        var rowBound = (RowBounds) invocation.getArgs()[2];
        var resultHandler = (ResultHandler) invocation.getArgs()[3];

        var sql = boundSql.getSql() + String.format(" AND (%s)", permissionSql.substring(4));
        var newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), parameter);
        var cacheKey = executor.createCacheKey(ms, parameter, rowBound, boundSql);
        return executor.query(ms, parameter, rowBound, resultHandler, null, newBoundSql);
    }
}
