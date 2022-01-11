package com.smart.auth.extensions.appsecret.service;

import com.smart.auth.core.secret.data.AppDetails;

/**
 * AccessToken creator
 * @author ShiZhongMing
 * @since 1.0
 */
public interface AccessTokenCreator {

    /**
     * 创建access token
     * @param appDetails app详情
     * @return access token
     */
    String createAccessToken(AppDetails appDetails);
}
