package com.smart.module.api.system.constants;

/**
 * 系统模块接口URL
 * @author zhongming4762
 * 2023/3/8
 */
public interface SystemApiUrlConstants {

    /**
     * 通过用户名获取认证用户
     */
    String GET_AUTH_USER_BY_USERNAME = "/remote/system/auth/getByUsername";

    /**
     * 通过电话获取用户
     */
    String GET_AUTH_USER_BY_PHONE = "/remote/system/auth/getByPhone";

    String QUERY_ROLE_PERMISSION = "/remote/system/auth/queryRolePermission";

    /**
     * 保存系统日志
     */
    String LOG_SAVE = "/remote/system/log/save";

    String LIST_USER_BY_USERNAME = "/remote/system/user/listUserByUsername";
    String LIST_USER_BY_ID = "/remote/system/user/listUserById";
    String LOCK_ACCOUNT = "/remote/system/user/lockAccount";
    String RESET_LOGIN_FAIL_TIME = "/remote/system/user/resetLoginFailTime";

    /**
     * 读取国际化信息
     */
    String I18N_READ_BY_LOCALE = "/remote/system/i18n/readI18nByLocale";

    String EXCEPTION_SAVE = "/remote/system/exception/save";

    /**
     * 获取系统参数接口
     */
    String PARAMETER_GET = "/remote/system/parameter/get";
    String PARAMETER_BATCH_GET = "/remote/system/parameter/batchGet";

    String WECHAT_GET_BY_APP_OPENID = "/remote/system/wechat/getByAppOpenid";

    String WECHAT_GET_BY_APP_UNIONID = "/remote/system/wechat/getByUnionid";
}
