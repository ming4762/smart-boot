package com.smart.auth.core.service;

import org.springframework.lang.NonNull;

/**
 * @author ShiZhongMing
 * 2021/6/2 14:21
 * @since 1.0
 */
public abstract class AbstractAuthCache<K, V> implements AuthCache<K, V> {

    private final String prefix;

    protected AbstractAuthCache(String prefix) {
        this.prefix = prefix;
    }

    protected String getKey(@NonNull String key) {
        return this.prefix + ":" + key;
    }
}
