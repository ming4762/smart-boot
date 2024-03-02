package com.smart.crud.constants;

import lombok.Getter;

/**
 * 用户属性
 * @author shizhongming
 * 2020/3/30 7:00 下午
 */
@Getter
public enum UserPropertyEnum {

    /**
     * 创建人员ID
     */
    CREATE_USER_ID("createUserId"),
    // 创建时间
    CREATE_TIME("createTime"),
    // 更新人员ID
    UPDATE_USER_ID("updateUserId"),
    // 更新时间
    UPDATE_TIME("updateTime"),
    CREATE_USER("createBy"),
    UPDATE_USER("updateBy"),

    // 删除
    DELETE_TIME("deleteTime"),
    DELETE_USER_ID("deleteUserId"),
    DELETE_BY("deleteBy")
    ;

    private final String name;

    UserPropertyEnum(String name) {
        this.name = name;
    }
}
