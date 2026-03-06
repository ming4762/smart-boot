package com.smart.framework.auth.core.constants;

import lombok.Getter;

/**
 * 默认认证路径枚举
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026/3/6 16:48
 * @since 5.0.0
 */
@Getter
public enum DefaultAuthUrlEnum {
    LOGIN("/auth/login", "登录路径"),
    LOGOUT("/auth/logout", "退出登录路径"),
    REFRESH("/auth/refresh", "刷新令牌路径"),
    TENANT_CHANGE("/auth/tenant/change", "切换租户")
    ;


    private final String url;

    private final String remark;

    DefaultAuthUrlEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
