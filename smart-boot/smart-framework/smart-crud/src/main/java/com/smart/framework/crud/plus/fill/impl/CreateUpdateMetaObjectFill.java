package com.smart.framework.crud.plus.fill.impl;

import com.smart.framework.crud.constants.CrudConstants;
import com.smart.framework.crud.constants.ModelPropertyEnum;
import com.smart.framework.crud.plus.fill.SmartMetaObjectFill;
import com.smart.framework.crud.service.UserProvider;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;

import java.time.ZonedDateTime;

/**
 * @author shizhongming
 * 2024/4/19 20:53
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class CreateUpdateMetaObjectFill implements SmartMetaObjectFill {

    private final UserProvider userProvider;

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject, MappedStatement mappedStatement) {
        if (!this.findTableInfo(metaObject).isWithInsertFill()) {
            return;
        }
        // 判断是否有相关字段，没有则不进行填充
        if (metaObject.hasSetter(ModelPropertyEnum.CREATE_USER_ID.getName()) || metaObject.hasSetter(ModelPropertyEnum.CREATE_TIME.getName()) || metaObject.hasSetter(ModelPropertyEnum.CREATE_USER.getName())) {
            this.strictInsertFill(metaObject, ModelPropertyEnum.CREATE_USER_ID.getName(), this.userProvider::getCurrentUserId, Long.class);
            this.strictInsertFill(metaObject, ModelPropertyEnum.CREATE_TIME.getName(), ZonedDateTime::now, ZonedDateTime.class);
            this.strictInsertFill(metaObject, ModelPropertyEnum.CREATE_USER.getName(), this.userProvider::getCurrentUserFullName, String.class);
        }
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject, MappedStatement mappedStatement) {
        if (!this.findTableInfo(metaObject).isWithUpdateFill()) {
            return;
        }
        // 判断是否是逻辑删除，逻辑删除不更新更新时间和更新人
        boolean isLogicDelete = CrudConstants.LOGIC_DELETE_METHODS.stream().anyMatch(item -> mappedStatement.getId().endsWith(item));
        if (isLogicDelete) {
            return;
        }
        this.strictUpdateFill(metaObject, ModelPropertyEnum.UPDATE_USER_ID.getName(), this.userProvider::getCurrentUserId, Long.class);
        this.strictUpdateFill(metaObject, ModelPropertyEnum.UPDATE_TIME.getName(), ZonedDateTime::now, ZonedDateTime.class);
        this.strictUpdateFill(metaObject, ModelPropertyEnum.UPDATE_USER.getName(), this.userProvider::getCurrentUserFullName, String.class);
    }
}
