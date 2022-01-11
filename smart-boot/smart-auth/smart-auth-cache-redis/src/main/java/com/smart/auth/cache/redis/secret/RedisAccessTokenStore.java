package com.smart.auth.cache.redis.secret;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.secret.data.AccessTokenStoreData;
import com.smart.auth.core.secret.store.AbstractAccessTokenStore;
import com.smart.starter.redis.service.RedisService;
import org.springframework.lang.NonNull;

import java.time.Duration;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
public class RedisAccessTokenStore extends AbstractAccessTokenStore {

    private RedisService redisService;

    public RedisAccessTokenStore(AuthProperties authProperties) {
        super(authProperties.getPrefix(), authProperties.getAppsecret());
    }


    @Override
    protected void doSave(@NonNull String key, @NonNull AccessTokenStoreData data, @NonNull Duration expire) {
        this.redisService.put(key, data, expire);
    }

    @Override
    protected void delete(@NonNull String key) {
        this.redisService.delete(key);
    }

    @Override
    protected boolean has(@NonNull String key) {
        return this.redisService.hasKey(key);
    }

    @Override
    protected AccessTokenStoreData get(@NonNull String key) {
        return this.redisService.get(key, AccessTokenStoreData.class);
    }
}
