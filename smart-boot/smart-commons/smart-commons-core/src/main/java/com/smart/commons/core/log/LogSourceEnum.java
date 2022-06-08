package com.smart.commons.core.log;

import lombok.Getter;

/**
 * @author jackson
 * 2020/1/22 2:34 下午
 */
@Getter
public enum LogSourceEnum {
    /**
     * 系统自动
     */
    AUTO_POINTCUT("10"),
    /**
     * 手动日志
     */
    MANUAL("20"),
    /**
     * 登录日志
     */
    LOGIN("30"),
    /**
     * 登出日志
     */
    LOGOUT("40"),
    /**
     * 登录失败日志
     */
    LOGIN_FAIL("50")
    ;

    private final String value;

    LogSourceEnum(String value) {
        this.value = value;
    }
}
