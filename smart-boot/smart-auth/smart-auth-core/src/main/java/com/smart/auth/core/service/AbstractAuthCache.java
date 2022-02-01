package com.smart.auth.core.service;

import org.springframework.lang.NonNull;

/**
 * @author ShiZhongMing
 * 2021/6/2 14:21
 * @since 1.0
 */
public abstract class AbstractAuthCache<K, V> implements AuthCache<K, V> {

    private static final String SPLIT = "&##&";

    private final String prefix;

    protected AbstractAuthCache(String prefix) {
        this.prefix = prefix;
    }

    protected String getKey(@NonNull String key) {
        return this.prefix + SPLIT + key;
    }

    /**
     * 获取实际的key
     * @param key 排除前缀
     * @return 实际的key
     */
    protected String getRealKey(@NonNull String key) {
        return key.split(SPLIT)[1];
    }
}
