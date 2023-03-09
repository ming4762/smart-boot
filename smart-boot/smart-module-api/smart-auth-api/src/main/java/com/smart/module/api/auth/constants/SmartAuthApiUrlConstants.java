package com.smart.module.api.auth.constants;

/**
 * 认证模块远程调用接口
 * @author zhongming4762
 * 2023/3/8
 */
public interface SmartAuthApiUrlConstants {

    /**
     * 用户离线，通过token
     */
    String OFFLINE_BY_TOKEN = "/remote/auth/getByUsername";

    /**
     * 用户离线，通过username
     */
    String OFFLINE_BY_USERNAME = "/remote/auth/offlineByUsername";

    /**
     * 通过token查询用户信息
     */
    String GET_USER_DETAILS_BY_TOKEN = "/remote/auth/getUserDetails";
}
