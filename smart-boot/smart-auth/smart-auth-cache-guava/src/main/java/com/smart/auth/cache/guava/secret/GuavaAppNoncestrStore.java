package com.smart.auth.cache.guava.secret;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.secret.store.AbstractAppNoncestrStore;
import com.smart.starter.cache.guava.GuavaCacheService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.time.Instant;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
public class GuavaAppNoncestrStore extends AbstractAppNoncestrStore {

    private final GuavaCacheService cacheService;

    public GuavaAppNoncestrStore(AuthProperties properties, GuavaCacheService cacheService) {
        super(properties.getPrefix(), properties.getAppsecret().getNoncestrTimeout());
        this.cacheService = cacheService;
    }

    @Override
    protected void doSave(@NonNull String key, @NonNull Instant now, @NonNull Duration timeout) {
        this.cacheService.put(key, now, timeout);
    }

    @Nullable
    @Override
    protected Instant get(@NonNull String key) {
        return (Instant) this.cacheService.get(key);
    }

    @Override
    protected void delete(@NonNull String key) {
        this.cacheService.delete(key);
    }
}
