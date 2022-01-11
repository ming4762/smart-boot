package com.smart.auth.cache.redis.secret;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.secret.store.AbstractAppNoncestrStore;
import com.smart.starter.redis.service.RedisService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.time.Instant;

/**
 * redis 随机串存储
 * @author ShiZhongMing
 * @since 1.0。7
 */
public class RedisAppNoncestrStore extends AbstractAppNoncestrStore {

    private final RedisService redisService;

    public RedisAppNoncestrStore(AuthProperties authProperties, RedisService redisService) {
        super(authProperties.getPrefix(), authProperties.getAppsecret().getNoncestrTimeout());
        this.redisService = redisService;
    }

    @Override
    protected void doSave(@NonNull String key, @NonNull Instant now, @NonNull Duration timeout) {
        this.redisService.put(key, now, timeout);
    }

    @Nullable
    @Override
    protected Instant get(@NonNull String key) {
        return this.redisService.get(key, Instant.class);
    }

    @Override
    protected void delete(@NonNull String key) {
        this.redisService.delete(key);
    }
}
