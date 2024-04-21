package com.smart.crud.plus.fill.impl;

import com.smart.crud.constants.CrudConstants;
import com.smart.crud.constants.ModelPropertyEnum;
import com.smart.crud.plus.fill.SmartMetaObjectFill;
import com.smart.crud.plus.metadata.SmartTableInfo;
import com.smart.crud.service.UserProvider;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author shizhongming
 * 2024/4/20 18:29
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class LogicDeleteMetaObjectFill implements SmartMetaObjectFill {

    private final UserProvider userProvider;


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
        this.fillStrategy(metaObject, ModelPropertyEnum.DELETE_BY.getName(), this.userProvider.getCurrentUserFullName());
        this.fillStrategy(metaObject, ModelPropertyEnum.DELETE_USER_ID.getName(), this.userProvider.getCurrentUserId());
        this.fillStrategy(metaObject, ModelPropertyEnum.DELETE_TIME.getName(), LocalDateTime.now());
    }

}
