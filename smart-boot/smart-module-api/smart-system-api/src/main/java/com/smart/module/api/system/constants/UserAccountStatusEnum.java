package com.smart.module.api.system.constants;

import lombok.Getter;

/**
 * 用户状态枚举
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0.7
 */
@Getter
public enum UserAccountStatusEnum {

    /**
     * 10:正常
     */
    NORMAL("10"),
    /**
     * 多次登录失败锁定
     */
    LOGIN_FAIL_LOCKED("20"),
    /**
     * 超出指定时间未登录锁定
     */
    LONG_TIME_LOCKED("30"),
    /**
     * 超出指定时间未修改密码锁定
     */
    LONG_TIME_PASSWORD_MODIFY_LOCKED("40")
    ;

    private final String value;

    UserAccountStatusEnum(String value) {
        this.value = value;
    }
}
