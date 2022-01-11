package com.smart.auth.extensions.appsecret.sign;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
public class DefaultSignProviderImpl implements SignProvider {
    @Override
    public String sign(AppsecretSignModel parameter) {
        // 构建字符串
        String signStr = String.format("accessToken=%s&noncestr=%s&timestamp=%s&url=%s", parameter.getAccessToken(), parameter.getNoncestr(), parameter.getTimestamp(), parameter.getUrl());
        return Hashing.sha256().newHasher().putString(signStr, StandardCharsets.UTF_8).hash().toString();
    }
}
