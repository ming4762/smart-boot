package com.smart.framework.crud.plus.fill.impl;

import com.smart.framework.crud.constants.CrudConstants;
import com.smart.framework.crud.constants.ModelPropertyEnum;
import com.smart.framework.crud.plus.fill.SmartMetaObjectFill;
import com.smart.framework.crud.plus.inner.LogicDeleteFieldInjectInnerInterceptor;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.module.api.crud.SmartCrudUserApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.ObjectProvider;

import java.time.ZonedDateTime;

/**
 * 逻辑删除元对象填充器
 * 自定义SQL通过{@link LogicDeleteFieldInjectInnerInterceptor} 进行填充
 * @author shizhongming
 * 2024/4/20 18:29
 * @since 3.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class LogicDeleteMetaObjectFill implements SmartMetaObjectFill {

    private final ObjectProvider<SmartCrudUserApi> smartCrudUserApiObjectProvider;


    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void logicDeleteFill(MetaObject metaObject, MappedStatement mappedStatement) {
        SmartTableInfo tableInfo = this.findTableInfo(metaObject);
        if (tableInfo == null || !tableInfo.isWithLogicDelete()) {
            return;
        }
        boolean isLogicDelete = CrudConstants.LOGIC_DELETE_METHODS.stream().anyMatch(item -> mappedStatement.getId().endsWith(item));
        if (!isLogicDelete) {
            return;
        }
        if (metaObject.hasSetter(ModelPropertyEnum.DELETE_BY.getName()) || metaObject.hasSetter(ModelPropertyEnum.DELETE_USER_ID.getName()) || metaObject.hasSetter(ModelPropertyEnum.DELETE_TIME.getName())) {
            SmartCrudUserApi smartCrudUserApi = this.smartCrudUserApiObjectProvider.getIfAvailable();
            if (smartCrudUserApi == null) {
                log.warn("smartCrudUserApi is null, can not inject logic delete user field");
            } else {
                this.fillStrategy(metaObject, ModelPropertyEnum.DELETE_BY.getName(), smartCrudUserApi.getCurrentUserFullName());
                this.fillStrategy(metaObject, ModelPropertyEnum.DELETE_USER_ID.getName(), smartCrudUserApi.getCurrentUserId());
            }
            this.fillStrategy(metaObject, ModelPropertyEnum.DELETE_TIME.getName(), ZonedDateTime.now());
        }
    }

}
