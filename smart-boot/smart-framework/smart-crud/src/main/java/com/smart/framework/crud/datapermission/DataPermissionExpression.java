package com.smart.framework.crud.datapermission;

import com.smart.framework.crud.model.BaseModel;

/**
 * @author shizhongming
 * 2025/1/6 20:21
 * @since 5.0.0
 */
public interface DataPermissionExpression {

    /**
     * 是否有数据权限
     * @param modelClass model class
     * @param field 字段
     * @param value 值
     * @return 是否有数据权限
     */
    boolean hasDataPermission(Class<? extends BaseModel> modelClass, String field, String value);
}
