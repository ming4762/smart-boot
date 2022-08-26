package com.smart.starter.redis.service;

import com.smart.starter.redis.constants.RedisInfoParameterEnum;
import com.smart.starter.redis.model.RedisKeySpace;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * redis服务层
 * @author shizhongming
 * 2020/1/17 8:47 下午
 */
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void matchDelete(@NonNull Object prefixKey) {
        final List<Object> keys = this.matchKeys(prefixKey);
        this.batchDelete(keys);
    }

    @Override
    public void put(@NonNull Object key, @NonNull Object value) {
        redisTemplate.opsForValue().set(key, value);

    }

    @Override
    public void put(@NonNull Object key, @NonNull Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void put(@NonNull Object key, @NonNull Object value, @NonNull Duration timeout) {
        this.redisTemplate.opsForValue().set(key, value, timeout);
    }

    @Override
    public void put(@NonNull Object key, @NonNull Object value, @NonNull Instant expireTime) {
        this.put(key, value);
        redisTemplate.expireAt(key, expireTime);
    }

    @Override
    public void batchPut(@NonNull Map<Object, Object> keyValues) {
        this.redisTemplate.opsForValue().multiSet(keyValues);
    }

    @Override
    public void batchPut(@NonNull Map<Object, Object> keyValues, long timeout) {
        this.batchPut(keyValues, Duration.ofSeconds(timeout));
    }

    @Override
    public void batchPut(@NonNull Map<Object, Object> keyValues, @NonNull Duration timeout) {
        this.redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            RedisSerializer keySerializer = this.redisTemplate.getKeySerializer();
            RedisSerializer valueSerializer = this.redisTemplate.getValueSerializer();
            keyValues.forEach((key, value) -> connection.stringCommands().set(keySerializer.serialize(key), valueSerializer.serialize(value), Expiration.from(timeout),  RedisStringCommands.SetOption.UPSERT));
            return null;
        });
    }

    /**
     * 设置key的过期时间
     * @param key key
     * @param timeout 过期时间
     */
    @Override
    public void expire(@NonNull Object key, long timeout) {
        this.redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void expire(@NonNull Object key, Duration timeout) {
        this.redisTemplate.expire(key, timeout);
    }

    /**
     * 批量设置key的过期时间
     * @param keys key
     * @param timeout 过期时间
     */
    @Override
    public void batchExpire(@NonNull Collection<Object> keys, long timeout) {
        keys.forEach(key -> this.expire(key, timeout));
    }

    @Override
    public void batchPut(@NonNull Map<Object, Object> keyValues, @NonNull Instant expireTime) {
        this.redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            RedisSerializer keySerializer = this.redisTemplate.getKeySerializer();
            RedisSerializer valueSerializer = this.redisTemplate.getValueSerializer();
            keyValues.forEach((key, value) -> connection.stringCommands().set(keySerializer.serialize(key), valueSerializer.serialize(value), Expiration.milliseconds(expireTime.toEpochMilli() - System.currentTimeMillis()),  RedisStringCommands.SetOption.UPSERT));
            return null;
        });
    }

    @Override
    public @Nullable Object get(@NonNull Object key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T get(@NonNull Object key, @NonNull Class<T> clazz) {
        return (T) this.get(key);
    }

    @Override
    public @Nullable List<Object> batchGet(@NonNull Collection<Object> keys) {
        return this.redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public @Nullable <T> List<T> batchGet(@NonNull Collection<Object> keys, @NonNull Class<T> clazz) {
        return (List<T>) this.redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void delete(@NonNull Object key) {
        this.redisTemplate.delete(key);
    }

    @Override
    public void batchDelete(@NonNull List<Object> keys) {
        this.redisTemplate.delete(keys);
    }


    /**
     * 匹配key
     * @param patternKey 所有key
     * @return 所有key
     */
    @Override
    public List<Object> matchKeys(@NonNull Object patternKey) {
        List<Object> result = new LinkedList<>();
        try (Cursor<byte[]> scan = this.scan(patternKey, null)) {
            while (scan.hasNext()) {
                result.add(this.redisTemplate.getKeySerializer().deserialize(scan.next()));
            }
            return result;
        }
    }

    @Override
    public boolean hasKey(@NonNull Object key) {
        return Boolean.TRUE.equals(this.redisTemplate.hasKey(key));
    }

    /**
     * 获取扫描对象
     * @param patternKey 所有key
     * @param count 获取数量
     * @return 扫描对象
     */
    private Cursor<byte[]> scan(@NonNull Object patternKey, Integer count) {
        // 创建扫描参数
        ScanOptions.ScanOptionsBuilder builder = ScanOptions.scanOptions()
                .match("*" + patternKey);
        if (Objects.nonNull(count)) {
            builder.count(count);
        }
        ScanOptions scanOptions = builder.build();
        // 获取连接信息
        final RedisConnectionFactory factory = this.redisTemplate.getConnectionFactory();
        Assert.notNull(factory, "获取redis连接失败");
        final RedisConnection redisConnection = factory.getConnection();
        return redisConnection.keyCommands().scan(scanOptions);
    }

    @Override
    public Properties info(@Nullable RedisInfoParameterEnum parameter) {
        RedisConnection redisConnection = this.redisTemplate.getRequiredConnectionFactory().getConnection();
        if (parameter == null) {
            return redisConnection.serverCommands().info();
        }
        return redisConnection.serverCommands().info(parameter.getParameter());
    }

    @Override
    @NonNull
    public Map<String, RedisKeySpace> queryKeySpace() {
        Properties properties = this.info(RedisInfoParameterEnum.KEYSPACE);

        Map<String, RedisKeySpace> result = new HashMap<>(properties.size());

        properties.forEach((key, value) -> result.put(key.toString(), this.analysisKeySpace(key.toString(), value.toString())));
        return result;
    }

    private RedisKeySpace analysisKeySpace(String db, String data) {
        List<String[]> analysisData = Arrays.stream(data.split(","))
                .map(item -> item.split("="))
                .collect(Collectors.toList());
        return new RedisKeySpace(db, Long.parseLong(analysisData.get(0)[1]), Long.parseLong(analysisData.get(1)[1]), Long.parseLong(analysisData.get(2)[1]));
    }

    @Override
    public RedisTemplate<Object, Object> getRedisTemplate() {
        return this.redisTemplate;
    }

    @Override
    public Long listSize(Object key) {
        return this.redisTemplate.opsForList().size(key);
    }

    @Override
    public Long listLeftPush(Object key, List<Object> dataList) {
        return this.redisTemplate.opsForList().leftPushAll(key, dataList);
    }

    @Override
    public Long listRightPush(Object key, List<Object> dataList) {
        return this.redisTemplate.opsForList().rightPushAll(key, dataList);
    }

    @Override
    public void listSet(Object key, long index, Object value) {
        this.redisTemplate.opsForList().set(key, index, value);
    }

    @Override
    public Long listRemove(Object key, long count, Object value) {
        return this.redisTemplate.opsForList().remove(key, count, value);
    }

    @Override
    public Object listIndex(Object key, long index) {
        return this.redisTemplate.opsForList().index(key, index);
    }

    @Override
    public List<Object> listRange(Object key, long start, long end) {
        return this.redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public Long hashDelete(Object key, List<Object> hashKeys) {
        return this.redisTemplate.opsForHash().delete(key, hashKeys.toArray());
    }

    @Override
    public Boolean hashHasKey(Object key, Object hashKey) {
        return this.redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public Object hashGet(Object key, Object hashKey) {
        return this.redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Set<Object> hashKeys(Object key) {
        return this.redisTemplate.opsForHash().keys(key);
    }

    @Override
    public Long hashSize(Object key) {
        return this.redisTemplate.opsForHash().size(key);
    }

    @Override
    public void hashPutAll(Object key, Map<?, ?> dataMap) {
        this.redisTemplate.opsForHash().putAll(key, dataMap);
    }

    @Override
    public void hashPut(Object key, Object hashKey, Object value) {
        this.redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public Map<Object, Object> hashEntries(Object key) {
        return this.redisTemplate.opsForHash().entries(key);
    }
}
