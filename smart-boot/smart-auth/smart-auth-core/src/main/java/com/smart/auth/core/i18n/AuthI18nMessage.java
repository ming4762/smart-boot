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
    // 账户未创建
    ACCOUNT_NOT_CREATED("auth.check.account.notCreated", "Account not created"),
    // 长时间未登录锁定
    ACCOUNT_NOT_LOGIN_LOCKED("auth.check.noLogin.locked", "Account locking without login for a long time"),
    // 长时间未修改密码账户锁定
    ACCOUNT_PASSWORD_NO_MODIFY_LOCKED("auth.check.password.noModify.locked", "Account lock without password modification"),
    // 登录IP不在白名单内
    ACCOUNT_IP_NOT_IN_WHITELIST("auth.check.ip.whitelist.error", "Login IP is not in the white list"),
    // 登录用户数量达到最大连接数，登录失败
    MAX_CONNECTION_LOGIN_FAIL("auth.check.login.maxConnection.error", "The login user has reached the maximum number of connections. Login is not allowed"),


    // --------- 修改密码错误 ---------------
    // 原密码错误
    PASSWORD_CHANGE_OLD_PASSWORD_ERROR("auth.error.changePassword.oldPasswordError", "old password error"),
    // 密码不一致
    PASSWORD_CHANGE_PASSWORD_INCONSISTENT("auth.error.changePassword.passwordInconsistent", "The password is inconsistent"),
    // 新旧密码一致
    PASSWORD_CHANGE_OLD_NEW_PASSWORD_SAME("auth.error.changePassword.oldNewPasswordSame", "The old and new passwords cannot be consistent"),

    // ---------- 验证码 ------------------
    CAPTCHA_EXPIRE_ERROR("auth.error.captcha_expire", "Verification code has expired"),
    CAPTCHA_ERROR("auth.error.captcha_error", "Verification code error"),

    // ----------- 微信登录 ------------
    WECHAT_APP_CONFIG_NOT_FOUND("auth.error.wechat.app.configNotFound", "Failed to obtain WeChat min app configuration"),
    WECHAT_APP_CONFIG_NOT_FOUND_APPID("auth.error.wechat.app.configNotFoundAppid", "Unable to find the configuration for the corresponding appid=[{0}] for the search. Please verify!"),
    // 不支持的登录类型
    WECHAT_LOGIN_TYPE_NOT_SUPPORT("auth.error.wechat.loginTypeNotSupport", "Unsupported WeChat login type"),
    WECHAT_USER_NOT_BOND("auth.error.wechat.userBindError", "WeChat user not bound"),
    WECHAT_APP_APPID_NOT_CONFIG("auth.error.wechat.app.appidNotConfig", "Appid not config"),

    // --------- access secret 认证
    ACCESS_SECRET_TOKEN_EMPTY("auth.access.secret.token.empty", "Authentication information does not exist"),
    ACCESS_SECRET_FORMAT_ERROR("auth.access.secret.token.format.error" ,"Authentication format error, expected {PREFIX}:{ACCESS_KEY}:{SIGN}"),
    ACCESS_SECRET_ACCESS_KEY_ERROR("auth.access.secret.accessKey.error", "Access key error"),
    ACCESS_SECRET_ACCESS_KEY_EXPIRE("auth.access.secret.accessKey", "Access key has expired"),
    ACCESS_SECRET_IP_UNAUTHORIZED("auth.access.secret.ip.unauthorized", "IP Unauthorized"),
    ACCESS_SECRET_SIGN_ERROR("auth.access.secret.sign.error", "Verification of signature failed, Please check if the signature is correct"),
    ACCESS_SECRET_DATE_ERROR("auth.access.secret.date.error", "Date format error"),
    ACCESS_SECRET_DATE_EXPIRE("auth.access.secret.date.expire", "Date has expired"),
    ACCESS_SECRET_NONCE_USED("auth.access.secret.nonce.used", "Random strings can only be used once"),
    ACCESS_SECRET_NONCE_ERROR("auth.access.secret.nonce.error", "Random string does not exist"),
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
