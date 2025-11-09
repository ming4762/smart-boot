package com.smart.framework.extension.dingtalk.client.impl;

import com.smart.framework.commons.core.cache.CacheService;
import com.smart.framework.extension.dingtalk.constants.DingtalkClientTypeEnum;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 钉钉客户端实现类，添加了缓存功能
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/4 18:53
 * @since 5.0.0
 */
public class CachedSmartDingtalkClientImpl extends DefaultSmartDingtalkClientImpl{

    private final CacheService cacheService;

    protected static final String ACCESS_TOKEN_KEY = "SMART_DINGTALK_CLIENT_ACCESS_TOKEN";

    protected final String keyPrefix;

    private String accessTokenCacheKey;

    public CachedSmartDingtalkClientImpl(DingtalkClientTypeEnum clientType, CacheService cacheService, String keyPrefix) {
        super(clientType);
        this.cacheService = cacheService;
        this.keyPrefix = keyPrefix;
    }

    public CachedSmartDingtalkClientImpl(DingtalkClientTypeEnum clientType, CacheService cacheService) {
        this(clientType, cacheService, null);
    }

    @Override
    public void setClientId(String clientId) {
        super.setClientId(clientId);
        this.accessTokenCacheKey = Stream.of(keyPrefix, ACCESS_TOKEN_KEY, clientId)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(":"));
    }

    /**
     * 检查应用token是否过期
     *
     * @return true表示过期，false表示未过期
     */
    @Override
    public boolean isAccessTokenExpired() {
        Duration expire = this.cacheService.getExpire(this.accessTokenCacheKey);
        if (expire == null) {
            return true;
        }
        return expire.isPositive();
    }

    /**
     * 更新应用token
     *
     * @param accessToken 新的应用token
     * @param expireIn    应用token的有效期
     */
    @Override
    public synchronized void updateAccessToken(String accessToken, Duration expireIn) {
        this.cacheService.put(this.accessTokenCacheKey, accessToken, expireIn.dividedBy(this.expireOffset));
    }
}
