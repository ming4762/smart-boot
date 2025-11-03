package com.smart.framework.extension.dingtalk.client.impl;

import com.smart.framework.extension.dingtalk.client.SmartDingtalkClient;
import com.smart.framework.extension.dingtalk.constants.DingtalkClientTypeEnum;
import lombok.Getter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 默认钉钉应用配置服务类实现
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/1 21:28
 * @since 5.0.0
 */
@Getter
public class DefaultSmartDingtalkClientImpl implements SmartDingtalkClient {

    private final DingtalkClientTypeEnum clientType;
    private final Lock accessTokenLock = new ReentrantLock();

    private String clientId;
    private String clientSecret;
    private String accessToken;
    private long expiresIn;

    public DefaultSmartDingtalkClientImpl(DingtalkClientTypeEnum clientType) {
        this.clientType = clientType;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * 检查应用token是否过期
     *
     * @return true表示过期，false表示未过期
     */
    @Override
    public boolean isAccessTokenExpired() {
        return System.currentTimeMillis() < expiresIn * 1000;
    }

    /**
     * 更新应用token
     *
     * @param accessToken 新的应用token
     * @param expiresIn   应用token的过期时间，单位秒
     */
    @Override
    public synchronized void updateAccessToken(String accessToken, long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
}
