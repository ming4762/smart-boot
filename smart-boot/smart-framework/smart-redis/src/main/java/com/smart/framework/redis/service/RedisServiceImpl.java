package com.smart.framework.redis.service;

import com.smart.framework.redis.constants.RedisInfoParameterEnum;
import com.smart.framework.redis.model.RedisKeySpace;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.redisson.api.*;
import org.redisson.api.options.KeysScanOptions;
import org.redisson.api.redisnode.RedisNode;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.api.redisnode.RedisSingle;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedissonClient redissonClient;

    @Override
    public void matchDelete(@NonNull String prefixKey) {
        final List<String> keys = this.matchKeys(prefixKey);
        this.batchDelete(keys);
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value) {
        redissonClient.getBucket(key).set(value);

    }

    @Override
    public void put(@NonNull String key, @NonNull Object value, @NonNull Duration timeout) {
        this.redissonClient.getBucket(key).set(value, timeout);
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value, @NonNull Instant expireTime) {
        this.put(key, value);
        this.redissonClient.getBucket(key).expire(expireTime);
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues) {
        if (CollectionUtils.isEmpty(keyValues)) {
            return;
        }
        RBatch batch = this.redissonClient.createBatch();
        keyValues.forEach((key, value) -> batch.getBucket(key).setAsync(value));
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
        keyValues.forEach((key, value) -> batch.getBucket(key).setAsync(value, timeout));
        batch.execute();
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues, @NonNull Instant expireTime) {
        if (CollectionUtils.isEmpty(keyValues)) {
            return;
        }
        RBatch batch = this.redissonClient.createBatch();
        keyValues.forEach((key, value) -> {
            RBucketAsync<Object> bucket = batch.getBucket(key);
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
        this.redissonClient.getBucket(key).expire(timeout);
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
        keys.forEach(key -> batch.getBucket(key).expireAsync(timeout));
        batch.execute();
    }

    @Override
    public <T> T get(@NonNull String key) {
        return this.redissonClient.<T>getBucket(key).get();
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
            RBucketAsync<T> bucket = batch.<T>getBucket(key);
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
        this.redissonClient.getBucket(key).delete();
    }

    @Override
    public void batchDelete(@NonNull List<String> keys) {
        // 创建批处理对象
        RBatch batch = redissonClient.createBatch();
        // 为每个 key 添加删除操作
        for (String key : keys) {
            batch.getBucket(key).deleteAsync();
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
        Iterable<String> stringIterable = keys.getKeys(KeysScanOptions.defaults().pattern(patternKey));

        return StreamSupport.stream(stringIterable.spliterator(), false)
                .toList();
    }

    @Override
    public boolean hasKey(@NonNull String key) {
        RKeys keys = this.redissonClient.getKeys();
        return keys.countExists(key) > 0;
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
        return this.redissonClient.getList(key).size();
    }

    @Override
    public void listLeftPush(String key, List<Object> dataList) {
        this.redissonClient.getList(key).addFirst(dataList);
    }

    @Override
    public void listRightPush(String key, List<Object> dataList) {
        this.redissonClient.getList(key).addLast(dataList);
    }

    @Override
    public void listSet(String key, int index, Object value) {
        this.redissonClient.getList(key).add(index, value);
    }

    @Override
    public boolean listRemove(String key, int count, Object value) {
        return this.redissonClient.getList(key).remove(value, count);
    }

    @Override
    public Object listIndex(String key, int index) {
        return this.redissonClient.getList(key).get(index);
    }

    @Override
    public <T> List<T> listRange(String key, int start, int end) {
        return this.redissonClient.<T>getList(key).range(start, end);
    }

    @Override
    public long hashDelete(String key, List<Object> hashKeys) {
        return this.redissonClient.getMap(key).fastRemove(hashKeys.toArray());
    }

    @Override
    public boolean hashHasKey(String key, Object hashKey) {
        return this.redissonClient.getMap(key).containsKey(hashKey);
    }

    @Override
    public <T> T hashGet(String key, Object hashKey) {
        return this.redissonClient.<Object, T>getMap(key).get(hashKey);
    }

    @Override
    public Set<Object> hashKeys(String key) {
        return this.redissonClient.getMap(key).keySet();
    }

    @Override
    public long hashSize(String key) {
        return this.redissonClient.getMap(key).size();
    }

    @Override
    public <K, V> void hashPutAll(String key, Map<? extends K, ? extends V> dataMap) {
        this.redissonClient.<K, V>getMap(key).putAll(dataMap);
    }

    @Override
    public void hashPut(String key, Object hashKey, Object value) {
        this.redissonClient.getMap(key).put(value, hashKey);
    }

    @Override
    public Map<Object, Object> hashEntries(String key) {
        return this.redissonClient.getMap(key).readAllMap();
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
        return this.redissonClient.<T>getBucket(key).getAndDelete();
    }
}
