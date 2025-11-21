package com.smart.framework.crud.plus.inner;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.smart.framework.crud.mybatis.model.LogicDeleteParameter;
import com.smart.framework.crud.plus.fill.impl.LogicDeleteMetaObjectFill;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.api.crud.SmartCrudUserApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.beans.factory.ObjectProvider;

import java.sql.SQLException;
import java.time.ZonedDateTime;

import static com.smart.framework.crud.constants.CrudConstants.LOGIC_DELETE_METHODS;
import static com.smart.framework.crud.constants.SmartCrudConstants.DELETE_FIELDS;

/**
 * 本拦截器主要用于注入自定义SQL的拦截，用于逻辑删除时注入删除人、删除时间等字段
 * 使用mybatis plus提供的函数删除则通过{@link LogicDeleteMetaObjectFill} 注入
 * @author shizhongming
 * 2024/4/20 16:40
 * @since 3.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class LogicDeleteFieldInjectInnerInterceptor implements InnerInterceptor {

    private final ObjectProvider<SmartCrudUserApi> smartCrudUserApiObjectProvider;

    @Override
    public void beforeUpdate(Executor executor, MappedStatement mappedStatement, Object parameter) throws SQLException {
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (sqlCommandType != SqlCommandType.UPDATE) {
            return;
        }
        if (parameter == null) {
            return;
        }
        SmartTableInfo tableInfo = CrudUtils.getTableInfo(mappedStatement.getParameterMap().getType());
        if (tableInfo == null || !tableInfo.isWithLogicDelete()) {
            return;
        }
        boolean isLogicDelete = LOGIC_DELETE_METHODS.stream().anyMatch(item -> mappedStatement.getId().endsWith(item));
        if (!isLogicDelete) {
            return;
        }
        if (!(parameter instanceof MapperMethod.ParamMap)) {
            return;
        }
        SmartCrudUserApi smartCrudUserApi = smartCrudUserApiObjectProvider.getIfAvailable();
        LogicDeleteParameter logicDeleteParameter = new LogicDeleteParameter();
        if (smartCrudUserApi == null) {
            log.warn("smartCrudUserApi is null, can not inject logic delete user field");
        } else {
            logicDeleteParameter.setDeleteBy(smartCrudUserApi.getCurrentUserFullName());
            logicDeleteParameter.setDeleteUserId(smartCrudUserApi.getCurrentUserId());
        }
        logicDeleteParameter.setDeleteTime(ZonedDateTime.now());
        ((MapperMethod.ParamMap) parameter).put(DELETE_FIELDS, logicDeleteParameter);
    }
}
