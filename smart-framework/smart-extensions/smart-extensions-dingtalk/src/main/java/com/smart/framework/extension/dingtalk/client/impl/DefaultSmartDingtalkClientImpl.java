package com.smart.framework.extension.dingtalk.client.impl;

import com.smart.framework.extension.dingtalk.client.SmartDingtalkClient;
import com.smart.framework.extension.dingtalk.constants.DingtalkClientTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
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

    /**
     * 应用token有效期偏移量，默认5秒，用于避免网络波动导致的token过期问题
     */
    protected final Duration expireOffset = Duration.ofSeconds(5);

    @Setter
    private String clientId;
    @Setter
    private String clientSecret;
    private String accessToken;
    private Duration expireIn;
    private Instant updateTime;

    public DefaultSmartDingtalkClientImpl(DingtalkClientTypeEnum clientType) {
        this.clientType = clientType;
    }

    /**
     * 检查应用token是否过期
     *
     * @return true表示过期，false表示未过期
     */
    @Override
    public boolean isAccessTokenExpired() {
        return this.updateTime.plus(expireIn).isAfter(Instant.now().plus(expireOffset));
    }

    /**
     * 更新应用token
     *
     * @param accessToken 新的应用token
     * @param expireIn 应用token的有效期
     */
    @Override
    public synchronized void updateAccessToken(String accessToken, Duration expireIn) {
        this.accessToken = accessToken;
        this.expireIn = expireIn;
        this.updateTime = Instant.now();
    }
}
