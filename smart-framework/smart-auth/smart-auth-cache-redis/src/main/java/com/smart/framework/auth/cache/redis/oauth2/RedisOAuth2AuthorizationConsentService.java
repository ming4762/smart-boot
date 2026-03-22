package com.smart.framework.auth.cache.redis.oauth2;

import com.smart.framework.auth.cache.redis.RedisAuthCache;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

/**
 * 基于 Redis 实现的 OAuth2 授权同意服务
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-23 18:46
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private static final String CACHE_KEY_PREFIX = "sso:oauth2:consent";

    private final RedisAuthCache redisAuthCache;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String clientId = authorizationConsent.getRegisteredClientId();
        String principalName = authorizationConsent.getPrincipalName();
        String cacheId = this.getCacheId(clientId, principalName);
        // 待完善功能：设置超时时间
        this.redisAuthCache.put(cacheId, authorizationConsent, null);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String clientId = this.getCacheId(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
        this.redisAuthCache.remove(clientId);
    }

    @Override
    public @Nullable OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return null;
    }

    /**
     * 获取缓存 Key
     * @param registeredClientId 客户端 ID
     * @param principalName      用户名称
     * @return 缓存 Key
     */
    protected String getCacheId(String registeredClientId, String principalName) {
        return String.join(":", CACHE_KEY_PREFIX, registeredClientId, principalName);
    }
}
