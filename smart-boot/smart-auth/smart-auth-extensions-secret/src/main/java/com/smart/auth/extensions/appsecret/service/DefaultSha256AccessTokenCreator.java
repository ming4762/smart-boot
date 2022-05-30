package com.smart.auth.extensions.appsecret.service;

import com.smart.auth.core.secret.data.AppDetails;
import com.smart.commons.core.utils.DigestUtils;
import com.smart.commons.core.utils.JsonUtils;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
public class DefaultSha256AccessTokenCreator implements AccessTokenCreator {
    @Override
    public String createAccessToken(AppDetails appDetails) {
        return DigestUtils.sha256(JsonUtils.toJsonString(appDetails) + System.currentTimeMillis(), 1);
    }
}
