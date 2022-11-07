package com.smart.auth.extensions.appsecret.sign;

import com.smart.commons.core.utils.DigestUtils;

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
        return DigestUtils.sha256(signStr, 1);
    }
}
