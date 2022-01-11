package com.smart.auth.extensions.appsecret.service;

import com.google.common.hash.Hashing;
import com.smart.auth.core.secret.data.AppDetails;
import com.smart.commons.core.utils.JsonUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
public class DefaultSha256AccessTokenCreator implements AccessTokenCreator {
    @Override
    public String createAccessToken(AppDetails appDetails) {
        return Hashing.sha256().newHasher().putString(JsonUtils.toJsonString(appDetails) + System.currentTimeMillis(), StandardCharsets.UTF_8).hash().toString();
    }
}
