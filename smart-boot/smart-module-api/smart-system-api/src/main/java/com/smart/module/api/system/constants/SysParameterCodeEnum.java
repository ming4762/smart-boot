package com.smart.module.api.system.constants;

import lombok.Getter;

/**
 * 系统参数code
 * @author zhongming4762
 * 2023/2/27
 */
@Getter
public enum SysParameterCodeEnum {

    /**
     * 机密文件类型
     */
    SECRECY_FILE_TYPE("sys.fileType.secrecy"),
    /**
     *
     * 账户设置-登录失败锁定次数
     */
    AUTH_LOGIN_FAIL_LOCK_TIME("sys.auth.account.loginFailLockTime"),

    /**
     * 密码输入错误自动解锁解锁 秒数
     */
    AUTH_PASSWORD_ERROR_UNLOCK_SECOND("sys.auth.account.passwordErrorUnlockSecond"),
    /**
     *
     * 账户设置-最大访问连接数，单个用户可同时登录的数量，0：不限制
     */
    AUTH_MAX_CONNECTIONS("sys.auth.account.maxConnections"),
    /**
     * 账户设置-指定天数未登录账户锁定，0：永不锁定
     */
    AUTH_MAX_DAYS_SINCE_LOGIN("sys.auth.account.maxDaysSinceLogin"),
    /**
     * 密码必须修改的天使，0：不限制
     */
    AUTH_PASSWORD_LIFE_DAYS("sys.auth.account.passwordLifeDays")
    ;
    private final String code;

    SysParameterCodeEnum(String code) {
        this.code = code;
    }
}
