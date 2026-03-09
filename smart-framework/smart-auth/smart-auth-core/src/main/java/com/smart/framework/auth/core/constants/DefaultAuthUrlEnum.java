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
    WEB_LOGIN("/auth/login", "登录路径"),
    WEB_LOGOUT("/auth/logout", "退出登录路径"),
    TOKEN_REFRESH("/auth/refresh", "刷新令牌路径"),
    TENANT_CHANGE("/auth/tenant/change", "切换租户"),
    SMS_CREATE_CODE("/auth/sms/createCode", "短信登录创建验证码路径"),
    SMS_LOGIN("/auth/sms/login", "短信登录路径"),
    WECHAT_MINIAPP_LOGIN("/auth/wechat/miniapp/login", "微信小程序登录路径"),
    WECHAT_MP_CREATE_QRCODE("/auth/wechat/mp/createQrcode", "微信公众号登录创建二维码路径"),
    WECHAT_MP_LOGIN("/auth/wechat/mp/login", "微信公众号登录路径")
    ;


    private final String url;

    private final String remark;

    DefaultAuthUrlEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
