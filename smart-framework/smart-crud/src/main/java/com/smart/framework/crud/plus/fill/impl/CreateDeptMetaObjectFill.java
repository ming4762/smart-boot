package com.smart.framework.crud.plus.fill.impl;

import com.smart.framework.crud.constants.ModelPropertyEnum;
import com.smart.framework.crud.plus.fill.SmartMetaObjectFill;
import com.smart.module.api.crud.SmartCrudUserApi;
import com.smart.module.api.crud.module.UserDeptData;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 创建部门元对象填充器
 * @author shizhongming
 * 2025/2/24 19:21
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class CreateDeptMetaObjectFill implements SmartMetaObjectFill {

    private final SmartCrudUserApi smartCrudUserApi;

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject      元对象
     * @param mappedStatement MappedStatement
     */
    @Override
    public void insertFill(MetaObject metaObject, MappedStatement mappedStatement) {
        if (!this.findTableInfo(metaObject).isWithInsertFill()) {
            return;
        }
        if (metaObject.hasSetter(ModelPropertyEnum.DEPT_ID.getName()) || metaObject.hasSetter(ModelPropertyEnum.DEPT_NAME.getName())) {
            UserDeptData currentDept = this.smartCrudUserApi.getCurrentDept();
            if (currentDept != null) {
                this.strictInsertFill(metaObject, ModelPropertyEnum.DEPT_ID.getName(), currentDept::getDeptId, Long.class);
                this.strictInsertFill(metaObject, ModelPropertyEnum.DEPT_NAME.getName(), currentDept::getDeptName, String.class);
            }
        }
    }
}
