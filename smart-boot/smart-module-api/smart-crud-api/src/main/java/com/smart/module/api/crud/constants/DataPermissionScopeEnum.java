package com.smart.module.api.crud.constants;

import lombok.Getter;

/**
 * 数据范围枚举
 * @author shizhongming
 * 2025/3/5 10:59
 * @since 5.0.0
 */
@Getter
public enum DataPermissionScopeEnum {

    /**
     * 数据权限范围,DATA_ALL：所有数据权限，DATA_DEPT：部门数据权限，DATA_DEPT_AND_CHILD：部门及下级数据权限，DATA_PERSONAL：个人数据权限
     */
    DATA_ALL("", "全部可见"),
    DATA_DEPT("dept_id", "部门可见"),
    DATA_DEPT_AND_CHILD("dept_id", "当前部门及下级可见"),
    DATA_PERSONAL("create_user_id", "本人可见"),
    DATA_CUSTOM("", "自定义");

    private final String column;

    private final String remark;

    DataPermissionScopeEnum(String column, String remark) {
        this.column = column;
        this.remark = remark;
    }
}
