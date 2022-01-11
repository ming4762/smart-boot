package com.smart.auth.cache.guava.secret;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.secret.data.AccessTokenStoreData;
import com.smart.auth.core.secret.store.AbstractAccessTokenStore;
import com.smart.starter.cache.guava.GuavaCacheService;
import org.springframework.lang.NonNull;

import java.time.Duration;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
public class GuavaAccessTokenStore extends AbstractAccessTokenStore {

    private final GuavaCacheService cacheService;

    public GuavaAccessTokenStore(AuthProperties authProperties, GuavaCacheService cacheService) {
        super(authProperties.getPrefix(), authProperties.getAppsecret());
        this.cacheService = cacheService;
    }

    @Override
    protected void doSave(@NonNull String key, @NonNull AccessTokenStoreData data, @NonNull Duration expire) {
        this.cacheService.put(key, data, expire);
    }

    @Override
    protected void delete(@NonNull String key) {
        this.cacheService.delete(key);
    }

    @Override
    protected boolean has(@NonNull String key) {
        return this.cacheService.hasKey(key);
    }

    @Override
    protected AccessTokenStoreData get(@NonNull String key) {
        return  (AccessTokenStoreData) this.cacheService.get(key);
    }
}
