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

    /**
     * 用户认证接口
     */
    String AUTH_AUTHENTICATE = "/remote/auth/authenticate";

    String SET_AUTH_CACHE = "/remote/auth/setAuthCache";

    String GET_AUTH_CACHE = "/remote/auth/getAuthCache";

    String REMOVE_AUTH_CACHE = "/remote/auth/removeAuthCache";

    /**
     * 验证码验证接口
     */
    String AUTH_CAPTCHA_GENERATE = "/remote/auth/captcha/generate";

    String AUTH_CAPTCHA_VALIDATE = "/remote/auth/captcha/validate";

    String AUTH_CAPTCHA_VALIDATE_TOKEN = "/remote/auth/captcha/validateToken";
}
