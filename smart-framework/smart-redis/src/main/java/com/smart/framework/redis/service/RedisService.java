package com.smart.framework.redis.service;

import com.smart.framework.commons.core.cache.CacheService;
import com.smart.framework.redis.constants.RedisInfoParameterEnum;
import com.smart.framework.redis.model.RedisKeySpace;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis服务层
 * @author zhongming
 */
public interface RedisService extends CacheService {

    /**
     * 查询redis info信息
     * @param parameter 参数
     * @return redis info信息
     */
    Map<String, String> info(@Nullable RedisInfoParameterEnum parameter);

    /**
     * 查询redis key space数据
     * @return redis key space数据
     */
    @NonNull
    Map<String, RedisKeySpace> queryKeySpace();

    /**
     * 获取List长度
     * @param key key
     * @return 长度
     */
    long listSize(String key);

    /**
     * 将数组插入到列表的头部，从左侧插入
     * @param key key
     * @param dataList 需要插入的数据
     */
    void listLeftPush(String key, List<Object> dataList);

    /**
     * 将数组插入到列表的头部，从右侧插入
     * @param key key
     * @param dataList 需要插入的数据
     */
    void listRightPush(String key, List<Object> dataList);

    /**
     * 在列表中index的位置设置value值
     * @param key 列表key
     * @param index 位置
     * @param value value值
     */
    void listSet(String key, int index, Object value);

    /**
     * 从存储在键中的列表中删除等于值的元素的第一个计数事件
     * @param key 列表key
     * @param count count> 0：删除等于从头到尾移动的值的元素
     *              count <0：删除等于从尾到头移动的值的元素
     *              count = 0：删除等于value的所有元素
     * @param value 需要删除的值
     * @return 删除的数量
     */
    boolean listRemove(String key, int count, Object value);

    /**
     * 获取list指定位置的值
     * @param key 列表key
     * @param index 位置
     * @return 值
     */
    Object listIndex(String key, int index);

    /**
     * 获取List指定范围的值
     * @param key 列表key
     * @param start 开始位置
     * @param end 结束位置，-1获取所有
     * @return 列表值
     */
    <T> List<T> listRange(String key, int start, int end);

    /**
     * 删除给定的哈希hashKeys
     * @param key key
     * @param hashKeys hash key
     * @return 删除的数量
     */
    long hashDelete(String key, List<Object> hashKeys);

    /**
     * 根据key删除hash内的所有元素
     * @param key key
     * @return 是否删除成功
     */
    boolean hashDelete(String key);

    /**
     * 设置hash的过期时间
     * @param key key
     * @param timeout 过期时间
     * @return 是否设置成功
     */
    boolean hashExpire(String key, Duration timeout);

    /**
     * 确定哈希hashKey是否存在
     * @param key key
     * @param hashKey hashKey
     * @return 是否存在
     */
    boolean hashHasKey(String key, Object hashKey);

    /**
     * 从哈希获取给定hashKey的值
     * @param key key
     * @param hashKey hashKey
     * @return value
     */
    <T> T hashGet(String key, Object hashKey);

    /**
     * 获取key所对应的散列表的key
     * @param key key
     * @return hash keys
     */
    Set<Object> hashKeys(String key);

    /**
     * 获取key所对应的散列表的大小个数
     * @param key key
     * @return 长度
     */
    long hashSize(String key);

    /**
     * 添加多个hash
     * @param key key
     * @param dataMap map
     */
    <K, V>void hashPutAll(String key, Map<? extends K, ? extends V> dataMap);

    /**
     * key
     * @param key key
     * @param hashKey hashKey
     * @param value value
     */
    void hashPut(String key, Object hashKey, Object value);

    /**
     * 获取hash所有值
     * @param key key
     * @return hash
     */
    <K, V> Map<K, V> hashEntries(String key);

    /**
     * 获取限流器
     * @param key 限流器key
     * @return 限流器
     */
    RRateLimiter getRateLimiter(String key);

     /**
     * 获取redisson client
     * @return redisson client
     */
    RedissonClient getRedissonClient();
}
