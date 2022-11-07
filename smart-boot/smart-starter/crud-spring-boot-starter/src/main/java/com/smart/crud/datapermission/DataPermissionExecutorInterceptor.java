package com.smart.crud.datapermission;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
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
        String permissionSql = DataPermissionHolder.getSql();
        if (StringUtils.isBlank(permissionSql)) {
            return invocation.proceed();
        }

        Executor executor = (Executor) invocation.getTarget();
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = ms.getBoundSql(parameter);
        RowBounds rowBound = (RowBounds) invocation.getArgs()[2];
        ResultHandler resultHandler = (ResultHandler) invocation.getArgs()[3];

        String sql = boundSql.getSql() + String.format(" AND (%s)", permissionSql.substring(4));
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), parameter);
        CacheKey cacheKey = executor.createCacheKey(ms, parameter, rowBound, boundSql);
        return executor.query(ms, parameter, rowBound, resultHandler, null, newBoundSql);
    }
}
