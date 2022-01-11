package com.smart.auth.core.constants;

/**
 * 标识认证类型
 * @author ShiZhongMing
 * 2021/3/5 8:52
 * @since 1.0
 */
public enum AuthTypeEnum {

    /**
     * SAML2登录
     */
    SAML2,
    /**
     * 用户名密码登录
     */
    USERNAME,
    /**
     * 开放API接口登录
     */
    APP_SECRET
}
