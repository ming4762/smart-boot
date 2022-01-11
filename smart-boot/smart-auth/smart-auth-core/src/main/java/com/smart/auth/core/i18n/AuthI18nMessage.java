package com.smart.auth.core.i18n;

import com.smart.commons.core.i18n.I18nMessage;

/**
 * @author ShiZhongMing
 * 2021/3/9 15:20
 * @since 1.0
 */
public enum AuthI18nMessage implements I18nMessage {
    /**
     * temp token为null
     */
    ERROR_TEMP_TOKEN_EMPTY("auth.error.tempToken.empty", "Temp Token validate fail，token is empty"),
    // 临时token过期
    ERROR_TEMP_TOKEN_EXPIRE("auth.error.tempToken.expire", "Temp Token validate fail，token is expire"),
    // temp token ip验证错误
    ERROR_TEMP_TOKEN_IP_FAIL("auth.error.tempToken.ipFail", "Temp Token validate fail： IP validate fail"),
    // 资源验证失败
    ERROR_TEMP_TOKEN_RESOURCE_FAIL("auth.error.tempToken.resourceFail", "Temp Token validate fail: resource validate fail"),
    // 临时令牌申请未设置资源
    ERROR_TEMP_TOKEN_APPLY_RESOURCE_NULL("auth.error.tempToken.apply.resourceNull", "Temp Token apply fail, resource is empty"),
    ERROR_TEMP_TOKEN_APPLY_RESOURCE_FAIL("auth.error.tempToken.apply.resourceFail", "Temp Token apply fail, resource validate fail"),

    // 用户名密码错误
    USERNAME_PASSWORD_ERROR("auth.error.usernamePasswordError", "username or password error"),
    // 用户不存在错误
    USER_NOT_FOUND_ERROR("auth.error.userNotFoundError", "user is not found"),
    // 用户名密码不能为null
    USERNAME_PASSWORD_NULL("auth.error.usernamePasswordEmpty", "username and password can not empty"),
    // 手机号、验证码不能为null
    PHONE_CODE_NULL("auth.error.phoneCodeEmpty", "phone and code can not empty"),
    // 手机号验证码错误
    PHONE_CODE_ERROR("auth.error.phoneCodeError", "phone or code error"),
    // 手机号null
    PHONE_NULL("auth.error.phoneEmpty", "phone can not empty"),
    // token过期
    ERROR_TOKEN_EMPTY("auth.error.token.empty", "not login, please login"),
    ERROR_TOKEN_EXPIRE("auth.error.token.expire", "token is expire, please login in again"),
    ERROR_IP_VALIDATE("auth.error.ip.validate", "The login IP is different from the access IP"),

    // ------------- 账户状态验证 ----------
    // 用户已锁定
    ACCOUNT_LOCKED("auth.check.account.locked", "User account is locked"),
    // 用户不可用
    ACCOUNT_DISABLED("auth.check.account.disabled", "User is disabled"),
    // 用户已过期
    ACCOUNT_EXPIRED("auth.check.account.expired", "User account has expired"),

    // --------- 修改密码错误 ---------------
    // 原密码错误
    PASSWORD_CHANGE_OLD_PASSWORD_ERROR("auth.error.changePassword.oldPasswordError", "old password error"),
    // 密码不一致
    PASSWORD_CHANGE_PASSWORD_INCONSISTENT("auth.error.changePassword.passwordInconsistent", "The password is inconsistent")
    ;


    private final String i18nCode;

    private final String defaultValue;

    AuthI18nMessage(String i18nCode, String defaultValue) {
        this.i18nCode = i18nCode;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getI18nCode() {
        return this.i18nCode;
    }

    @Override
    public String defaultMessage() {
        return this.defaultValue;
    }


}
