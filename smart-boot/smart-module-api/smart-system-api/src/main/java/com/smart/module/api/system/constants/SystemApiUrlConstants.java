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
    String GET_AUTH_USER_BY_USERNAME = "/api/system/auth/getByUsername";

    /**
     * 通过电话获取用户
     */
    String GET_AUTH_USER_BY_PHONE = "/api/system/auth/getByPhone";

    String QUERY_ROLE_PERMISSION = "/api/system/auth/queryRolePermission";
}
