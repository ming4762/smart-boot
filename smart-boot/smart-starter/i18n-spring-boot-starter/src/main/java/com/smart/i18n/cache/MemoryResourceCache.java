package com.smart.i18n.cache;

import org.springframework.lang.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 内存ResourceCache
 * @author shizhongming
 * 2021/2/1 11:43 下午
 */
public class MemoryResourceCache implements ResourceCache {

    private static final ConcurrentMap<Locale, Map<String, String>> CACHE = new ConcurrentHashMap<>();

    @Override
    public void clear() {
        CACHE.clear();
    }

    @Override
    public Map<String, String> get(@NonNull Locale locale) {
        return CACHE.get(locale);
    }

    @Override
    public boolean contain(@NonNull Locale locale) {
        return CACHE.containsKey(locale);
    }

    @Override
    public String get(@NonNull Locale locale, @NonNull String key) {
        return Optional.ofNullable(CACHE.get(locale)).map(item -> item.get(key)).orElse(null);
    }

    @Override
    public void put(@NonNull Locale locale, @NonNull String key, @NonNull String value) {
        final Map<String, String> values = CACHE.get(locale);
        if (Objects.isNull(values)) {
            this.putAll(locale, new HashMap<>(0));
        }
        CACHE.get(locale).put(key, value);
    }

    @Override
    public void putAll(@NonNull Locale locale, @NonNull Map<String, String> value) {
        CACHE.put(locale, value);
    }

    @Override
    public String remove(@NonNull Locale locale, @NonNull String key) {
        if (this.contain(locale)) {
           return CACHE.get(locale).remove(key);
        }
        return null;
    }

    /**
     * 删除缓存
     * @param locale Locale
     * @return 移除的值
     */
    @Override
    public Map<String, String> remove(@NonNull Locale locale) {
        return CACHE.remove(locale);
    }
}
