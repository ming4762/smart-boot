package com.smart.framework.redis.service;

import com.smart.framework.commons.core.cache.AbstractCacheService;
import com.smart.framework.redis.constants.RedisInfoParameterEnum;
import com.smart.framework.redis.model.RedisKeySpace;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.redisson.api.*;
import org.redisson.api.options.KeysScanOptions;
import org.redisson.api.redisnode.RedisNode;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.api.redisnode.RedisSingle;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

/**
 * redis服务层
 * @author shizhongming
 * 2020/1/17 8:47 下午
 */
public class RedisServiceImpl extends AbstractCacheService implements RedisService {

    private static final String MATCH_STR = "*";

    private final RedissonClient redissonClient;

    public RedisServiceImpl(String keyPrefix, RedissonClient redissonClient) {
        super(keyPrefix);
        this.redissonClient = redissonClient;
    }

    @Override
    public void matchDelete(@NonNull String prefixKey) {
        final List<String> keys = this.matchKeys(this.getCachedKey(prefixKey));
        this.batchDelete(keys);
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value) {
        redissonClient.getBucket(getCachedKey(key)).set(value);

    }

    @Override
    public void put(@NonNull String key, @NonNull Object value, @NonNull Duration timeout) {
        this.redissonClient.getBucket(getCachedKey(key)).set(value, timeout);
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value, @NonNull Instant expireTime) {
        this.put(key, value);
        this.redissonClient.getBucket(getCachedKey(key)).expire(expireTime);
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues) {
        if (CollectionUtils.isEmpty(keyValues)) {
            return;
        }
        RBatch batch = this.redissonClient.createBatch();
        keyValues.forEach((key, value) -> batch.getBucket(getCachedKey(key)).setAsync(value));
        batch.execute();
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues, long timeout) {
        this.batchPut(keyValues, Duration.ofSeconds(timeout));
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues, @NonNull Duration timeout) {
        if (CollectionUtils.isEmpty(keyValues)) {
            return;
        }
        RBatch batch = this.redissonClient.createBatch();
        keyValues.forEach((key, value) -> batch.getBucket(getCachedKey(key)).setAsync(value, timeout));
        batch.execute();
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues, @NonNull Instant expireTime) {
        if (CollectionUtils.isEmpty(keyValues)) {
            return;
        }
        RBatch batch = this.redissonClient.createBatch();
        keyValues.forEach((key, value) -> {
            RBucketAsync<Object> bucket = batch.getBucket(getCachedKey(key));
            bucket.setAsync(value);
            bucket.expireAsync(expireTime);
        });
        batch.execute();
    }

    /**
     * 设置key的过期时间
     * @param key key
     * @param timeout 过期时间
     */
    @Override
    public void expire(@NonNull String key, Duration timeout) {
        this.redissonClient.getBucket(getCachedKey(key)).expire(timeout);
    }

    /**
     * 批量设置key过期时间
     *
     * @param keys    key列表
     * @param timeout 过期时间
     */
    @Override
    public void batchExpire(@NonNull Collection<String> keys, Duration timeout) {
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        RBatch batch = this.redissonClient.createBatch();
        keys.forEach(key -> batch.getBucket(getCachedKey(key)).expireAsync(timeout));
        batch.execute();
    }

    @Override
    public <T> T get(@NonNull String key) {
        return this.redissonClient.<T>getBucket(getCachedKey(key)).get();
    }

    @SneakyThrows({InterruptedException.class, ExecutionException.class})
    @Override
    public @Nullable <T> List<T> batchGet(@NonNull Collection<String> keys) {
        // 创建批处理对象
        RBatch batch = redissonClient.createBatch();

        // 存储批量读取的结果
        List<RFuture<T>> asyncList = new ArrayList<>();

        // 为每个 key 添加批量读取操作
        for (String key : keys) {
            RBucketAsync<T> bucket = batch.getBucket(getCachedKey(key));
            RFuture<T> async = bucket.getAsync();
            asyncList.add(async);
        }
        // 执行批处理
        batch.execute();

        // 从结果中提取值
        List<T> values = new ArrayList<>();
        for (RFuture<T> future : asyncList) {
            values.add(future.get());
        }
        return values;
    }

    @Override
    public void delete(@NonNull String key) {
        this.redissonClient.getBucket(getCachedKey(key)).delete();
    }

    @Override
    public void batchDelete(@NonNull List<String> keys) {
        // 创建批处理对象
        RBatch batch = redissonClient.createBatch();
        // 为每个 key 添加删除操作
        for (String key : keys) {
            batch.getBucket(getCachedKey(key)).deleteAsync();
        }
        // 执行批处理
        batch.execute();
    }


    /**
     * 匹配key
     *
     * @param patternKey 所有key
     * @return 所有key
     */
    @Override
    public List<String> matchKeys(@NonNull String patternKey) {
        RKeys keys = this.redissonClient.getKeys();
        Iterable<String> stringIterable = keys.getKeys(KeysScanOptions.defaults().pattern(this.getCachedKey(patternKey) + MATCH_STR));
        int subLength = this.getKeyPrefix().length();
        return StreamSupport.stream(stringIterable.spliterator(), false)
                .map(item -> item.substring(subLength))
                .toList();
    }

    @Override
    public boolean hasKey(@NonNull String key) {
        RKeys keys = this.redissonClient.getKeys();
        return keys.countExists(getCachedKey(key)) > 0;
    }

    @Override
    public Map<String, String> info(@Nullable RedisInfoParameterEnum parameter) {
        // TODO：待完善，只支持单一节点
        RedisSingle redisNodes = this.redissonClient.getRedisNodes(RedisNodes.SINGLE);

        RedisNode.InfoSection infoSection = parameter == null ? RedisNode.InfoSection.ALL : RedisNode.InfoSection.valueOf(parameter.name());
        return redisNodes.getInstance().info(infoSection);
    }

    @Override
    @NonNull
    public Map<String, RedisKeySpace> queryKeySpace() {
        Map<String, String> info = this.info(RedisInfoParameterEnum.KEYSPACE);
        if (CollectionUtils.isEmpty(info)) {
            return Collections.emptyMap();
        }

        Map<String, RedisKeySpace> result = HashMap.newHashMap(info.size());

        info.forEach((key, value) -> result.put(key, this.analysisKeySpace(key, value)));
        return result;
    }

    private RedisKeySpace analysisKeySpace(String db, String data) {
        List<String[]> analysisData = Arrays.stream(data.split(","))
                .map(item -> item.split("=")).toList();
        return new RedisKeySpace(db, Long.parseLong(analysisData.get(0)[1]), Long.parseLong(analysisData.get(1)[1]), Long.parseLong(analysisData.get(2)[1]));
    }

    @Override
    public long listSize(String key) {
        return this.redissonClient.getList(getCachedKey(key)).size();
    }

    @Override
    public void listLeftPush(String key, List<Object> dataList) {
        this.redissonClient.getList(getCachedKey(key)).addFirst(dataList);
    }

    @Override
    public void listRightPush(String key, List<Object> dataList) {
        this.redissonClient.getList(getCachedKey(key)).addLast(dataList);
    }

    @Override
    public void listSet(String key, int index, Object value) {
        this.redissonClient.getList(getCachedKey(key)).add(index, value);
    }

    @Override
    public boolean listRemove(String key, int count, Object value) {
        return this.redissonClient.getList(getCachedKey(key)).remove(value, count);
    }

    @Override
    public Object listIndex(String key, int index) {
        return this.redissonClient.getList(getCachedKey(key)).get(index);
    }

    @Override
    public <T> List<T> listRange(String key, int start, int end) {
        return this.redissonClient.<T>getList(getCachedKey(key)).range(start, end);
    }

    @Override
    public long hashDelete(String key, List<Object> hashKeys) {
        return this.redissonClient.getMap(getCachedKey(key)).fastRemove(hashKeys.toArray());
    }

    /**
     * 根据key删除hash内的所有元素
     *
     * @param key key
     */
    @Override
    public boolean hashDelete(String key) {
        return this.redissonClient.getMap(getCachedKey(key)).delete();
    }

    /**
     * 设置hash的过期时间
     *
     * @param key     key
     * @param timeout 过期时间
     */
    @Override
    public boolean hashExpire(String key, Duration timeout) {
        return this.redissonClient.getMap(getCachedKey(key)).expire(timeout);
    }

    @Override
    public boolean hashHasKey(String key, Object hashKey) {
        return this.redissonClient.getMap(getCachedKey(key)).containsKey(hashKey);
    }

    @Override
    public <T> T hashGet(String key, Object hashKey) {
        return this.redissonClient.<Object, T>getMap(getCachedKey(key)).get(hashKey);
    }

    @Override
    public Set<Object> hashKeys(String key) {
        return this.redissonClient.getMap(getCachedKey(key)).keySet();
    }

    @Override
    public long hashSize(String key) {
        return this.redissonClient.getMap(getCachedKey(key)).size();
    }

    @Override
    public <K, V> void hashPutAll(String key, Map<? extends K, ? extends V> dataMap) {
        this.redissonClient.<K, V>getMap(getCachedKey(key)).putAll(dataMap);
    }

    @Override
    public void hashPut(String key, Object hashKey, Object value) {
        this.redissonClient.getMap(this.getCachedKey(key)).put(hashKey, value);
    }

    @Override
    public <K, V> Map<K, V> hashEntries(String key) {
        return this.redissonClient.<K, V>getMap(this.getCachedKey(key)).readAllMap();
    }

    /**
     * 获取限流器
     * @param key 限流器key
     * @return 限流器
     */
    @Override
    public RRateLimiter getRateLimiter(String key) {
        return this.redissonClient.getRateLimiter(this.getCachedKey(key));
    }

    /**
     * 获取锁
     *
     * @param key 锁key
     * @return 锁
     */
    @Override
    public RLock getLock(@NonNull String key) {
        return this.redissonClient.getLock(this.getCachedKey(key));
    }

    @Override
    public RedissonClient getRedissonClient() {
        return this.redissonClient;
    }

    /**
     * 获取并删除
     *
     * @param key key
     * @return 数据
     */
    @Override
    public <T> T getAndRemove(@NonNull String key) {
        return this.redissonClient.<T>getBucket(this.getCachedKey(key)).getAndDelete();
    }

    @Override
    public void rename(String oldKey, String newKey) {
        RKeys keys = this.redissonClient.getKeys();
        keys.rename(oldKey, newKey);
    }

    /**
     * 获取缓存过期时间
     *
     * @param key key
     * @return 过期时间，null代表无限大
     */
    @Override
    public Duration getExpire(@NonNull String key) {
        RBucket<Object> bucket = this.redissonClient.getBucket(this.getCachedKey(key));
        long remainTimeToLive = bucket.remainTimeToLive();
        if (remainTimeToLive == -1) {
            // 永不过期
            return null;
        }
        if (remainTimeToLive == -2) {
            return Duration.ZERO;
        }
        return Duration.ofMillis(remainTimeToLive);
    }
}
