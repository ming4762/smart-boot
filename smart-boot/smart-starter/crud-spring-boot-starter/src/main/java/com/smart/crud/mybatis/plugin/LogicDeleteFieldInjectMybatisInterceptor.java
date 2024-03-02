package com.smart.crud.mybatis.plugin;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.smart.crud.mybatis.model.LogicDeleteParameter;
import com.smart.crud.service.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.time.LocalDateTime;
import java.util.List;

import static com.smart.crud.constants.SmartCrudConstants.DELETE_FIELDS;

/**
 * 动态注入逻辑删除参数
 * @author shizhongming
 * 2023/3/2 17:40
 * @since 3.0.0
 */
@Slf4j
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class LogicDeleteFieldInjectMybatisInterceptor implements Interceptor {

    private static final List<String> LOGIC_DELETE_METHODS = List.of(
            SqlMethod.DELETE_BY_ID.getMethod(),
            SqlMethod.DELETE_BATCH_BY_IDS.getMethod(),
            SqlMethod.DELETE.getMethod(),
            SqlMethod.DELETE_BY_MAP.getMethod()
    );

    private final UserProvider userProvider;

    public LogicDeleteFieldInjectMybatisInterceptor(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (sqlCommandType != SqlCommandType.UPDATE) {
            return invocation.proceed();
        }
        Object parameter = invocation.getArgs()[1];
        if (parameter == null) {
            // 没有参数不做任何操作
            return invocation.proceed();
        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(mappedStatement.getParameterMap().getType());
        if (tableInfo == null || !tableInfo.isWithLogicDelete()) {
            return invocation.proceed();
        }
        boolean isLogicDelete = LOGIC_DELETE_METHODS.stream().anyMatch(item -> mappedStatement.getId().endsWith(item));
        if (!isLogicDelete) {
            return invocation.proceed();
        }
        if (!(parameter instanceof MapperMethod.ParamMap)) {
            return invocation.proceed();
        }
        LogicDeleteParameter logicDeleteParameter = new LogicDeleteParameter();
        logicDeleteParameter.setDeleteBy(userProvider.getCurrentUserFullName());
        logicDeleteParameter.setDeleteUserId(userProvider.getCurrentUserId());
        logicDeleteParameter.setDeleteTime(LocalDateTime.now());
        ((MapperMethod.ParamMap) parameter).put(DELETE_FIELDS, logicDeleteParameter);
        return invocation.proceed();
    }
}
