package com.smart.module.api.system.constants;

import lombok.Getter;

/**
 * 超出最大连接数执行策略
 * @author ShiZhongMing
 * 2022/4/2
 * @since 2.0
 */
@Getter
public enum MaxConnectionsPolicyEnum {
    /**
     * 超出最大连接数执行策略
     */
    LOGIN_NOT_ALLOW("10", "不允许登录"),
    FIRST_USER_LOGOUT("20", "最早登录用户登出")
    ;

    private final String value;

    private final String message;

    MaxConnectionsPolicyEnum(String value, String message) {
        this.value = value;
        this.message = message;
    }
}
