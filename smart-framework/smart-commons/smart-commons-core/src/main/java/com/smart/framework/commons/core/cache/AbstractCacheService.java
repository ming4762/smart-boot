package com.smart.framework.commons.core.cache;

/**
 * @author shizhongming
 * 2025/7/30 14:05
 * @since 5.0.0
 */
public abstract class AbstractCacheService implements CacheService {

    /**
     * 缓存key前缀
     */
    private String keyPrefix;

    protected AbstractCacheService(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    /**
     * 获取缓存key
     * @param key key
     * @return 缓存key
     */
    public String getCachedKey(String key) {
        return this.keyPrefix + key;
    }

    protected String getKeyPrefix() {
        return this.keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}
