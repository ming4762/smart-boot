package com.smart.framework.auth.cache.redis;


import com.smart.framework.auth.core.service.AbstractAuthCache;
import com.smart.framework.redis.service.RedisService;
import lombok.SneakyThrows;
import org.redisson.api.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/7/1 3:54 下午
 */
public class RedisAuthCache extends AbstractAuthCache<Object> {

    private final RedisService cacheService;
    private final RedissonClient redissonClient;

    public RedisAuthCache(RedisService cacheService, String prefix) {
        super(prefix);
        this.cacheService = cacheService;
        this.redissonClient = cacheService.getRedissonClient();
    }

    /**
     * 添加缓存
     *
     * @param key     key
     * @param mapKey  mapKey
     * @param value   value
     * @param timeout 超时时间
     */
    @Override
    public void put(@NonNull String key, @NonNull String mapKey, @NonNull Object value, Duration timeout) {
        RMap<Object, Object> map = this.redissonClient.getMap(this.getKey(key));
        map.put(mapKey, value);
        map.expire(timeout);
    }

    /**
     * 添加缓存
     *
     * @param key     key
     * @param value   value
     * @param timeout 超时时间
     */
    @Override
    public void put(@NonNull String key, @NonNull Object value, Duration timeout) {
        this.cacheService.put(this.getKey(key), value, timeout);
    }

    /**
     * 添加缓存
     *
     * @param key     key
     * @param value   value
     * @param timeout 超时时间
     */
    @Override
    public void putAll(@NonNull String key, @NonNull Map<String, Object> value, Duration timeout) {
        RMap<Object, Object> map = this.redissonClient.getMap(this.getKey(key));
        map.putAll(value);
        map.expire(timeout);
    }

    /**
     * 设置超时时间
     * @param key key
     * @param timeout 超时时间
     */
    @Override
    public void expire(@NonNull String key, Duration timeout) {
        this.redissonClient.getMap(this.getKey(key)).expire(timeout);
    }

    /**
     * 获取缓存内容
     * @param key key
     * @return value
     */
    @Override
    @Nullable
    public Map<String, Object> get(@NonNull String key) {
        return this.redissonClient.<String, Object>getMap(this.getKey(key)).readAllMap();
    }

    /**
     * 获取缓存内容
     *
     * @param key key
     * @return value
     */
    @Override
    public Object getValue(@NonNull String key) {
        return this.cacheService.get(this.getKey(key));
    }

    /**
     * 获取缓存内容
     *
     * @param key    key
     * @param mapKey mapKey
     * @return value
     */
    @Override
    public Object get(@NonNull String key, @NonNull String mapKey) {
        return this.redissonClient.getMap(this.getKey(key)).get(mapKey);
    }

    /**
     * 删除缓存
     * @param key key
     */
    @Override
    public void remove(@NonNull String key) {
        this.redissonClient.getMap(this.getKey(key)).delete();
    }

    /**
     * 获取所有key的集合
     * @return key的集合
     */
    @Override
    public Set<String> keys() {
        return this.matchKeys("");
    }


    /**
     * 批量获取
     * @param keys keys
     * @return 获取的缓存
     */
    @SneakyThrows({InterruptedException.class, ExecutionException.class})
    @Override
    @NonNull
    public List<Map<String, Object>> batchGet(@NonNull Collection<String> keys) {
        RBatch batch = this.redissonClient.createBatch();
        // 存储批量读取的结果
        List<RFuture<Map<String, Object>>> asyncList = new ArrayList<>();
        for (String key : keys) {
            RMapAsync<String, Object> map = batch.<String, Object>getMap(this.getKey(key));
            RFuture<Map<String, Object>> async = map.readAllMapAsync();
            asyncList.add(async);
        }
        batch.execute();
        List<Map<String, Object>> dataList = new ArrayList<>(asyncList.size());
        for (RFuture<Map<String, Object>> async : asyncList) {
            dataList.add(async.get());
        }
        return dataList;
    }

    @Override
    public void matchRemove(@NonNull String matchKey) {
        List<String> keys = this.cacheService.matchKeys(matchKey);
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        // 创建批处理对象
        RBatch batch = redissonClient.createBatch();
        for (String key : keys) {
            batch.getMap(key).deleteAsync();
        }
        batch.execute();
    }

    @SneakyThrows({InterruptedException.class, ExecutionException.class})
    @Override
    public List<Map<String, Object>> matchGet(@NonNull String matchKey) {
        List<String> keys = this.cacheService.matchKeys(this.getKey(matchKey));
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        // 创建批处理对象
        RBatch batch = redissonClient.createBatch();
        // 存储批量读取的结果
        List<RFuture<Map<String, Object>>> asyncList = new ArrayList<>();
        for (String key : keys) {
            RFuture<Map<String, Object>> async = batch.<String, Object>getMap(this.getKey(key)).readAllMapAsync();
            asyncList.add(async);
        }
        // 执行批处理
        batch.execute();
        List<Map<String, Object>> dataList = new ArrayList<>(asyncList.size());
        for (RFuture<Map<String, Object>> future : asyncList) {
            dataList.add(future.get());
        }
        return dataList;
    }

    @Override
    public Set<String> matchKeys(@NonNull String matchKey) {
        return this.cacheService.matchKeys(this.getKey(matchKey))
                .stream()
                .map(this::getRealKey)
                .collect(Collectors.toSet());
    }

    /**
     * 获取并删除
     *
     * @param key key
     * @return 数据
     */
    @Override
    public Map<String, Object> getAndRemove(@NonNull String key) {
        Map<String, Object> data = this.redissonClient.<String, Object>getMap(this.getKey(key)).readAllMap();
        this.redissonClient.getMap(this.getKey(key)).delete();
        return data;
    }

    /**
     * 重命名
     *
     * @param oldKey 旧key
     * @param newKey 新key
     */
    @Override
    public void rename(@NonNull String oldKey, @NonNull String newKey) {
        this.cacheService.rename(this.getKey(oldKey), this.getKey(newKey));
    }

    /**
     * 是否存在key
     *
     * @param key key
     * @return 是否存在
     */
    @Override
    public boolean hasKey(@NonNull String key) {
        return this.cacheService.hasKey(this.getKey(key));
    }
}
