package com.smart.crud.plus.injector.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.smart.crud.plus.logic.LogicKeyStrategy;
import com.smart.crud.plus.metadata.TableLogicKeyFieldInfo;
import com.smart.crud.utils.CrudUtils;

import java.util.Objects;

/**
 * @author shizhongming
 * 2023/10/31 14:09
 * @since 3.0.0
 */
public abstract class AbstractSmartMethod extends AbstractMethod {
    /**
     * @param methodName 方法名
     * @since 3.5.0
     */
    protected AbstractSmartMethod(String methodName) {
        super(methodName);
    }

    /**
     * 获取设置 逻辑删除key SQL
     * @param tableInfo tableinfo
     * @return SQL
     */
    protected String sqlLogicKeySet(TableInfo tableInfo) {
        TableLogicKeyFieldInfo logicKeyField = Objects.requireNonNull(CrudUtils.getTableLogicKeyField(tableInfo));
        if (logicKeyField.getTableLogicKey().strategy().equals(LogicKeyStrategy.ID)) {
            return String.format("%s = %s", logicKeyField.getFieldInfo().getColumn(), tableInfo.getKeyColumn());
        }
        return "";
    }
}
