package com.smart.starter.redis.service;

import com.smart.commons.core.cache.CacheService;
import com.smart.starter.redis.constants.RedisInfoParameterEnum;
import com.smart.starter.redis.model.RedisKeySpace;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Properties;
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
    Properties info(@Nullable RedisInfoParameterEnum parameter);

    /**
     * 查询redis key space数据
     * @return redis key space数据
     */
    @NonNull
    Map<String, RedisKeySpace> queryKeySpace();

    /**
     * 获取redisTemplate
     * @return RedisTemplate
     */
    RedisTemplate<Object, Object> getRedisTemplate();

    /**
     * 获取List长度
     * @param key key
     * @return 长度
     */
    Long listSize(Object key);

    /**
     * 将数组插入到列表的头部，从左侧插入
     * @param key key
     * @param dataList 需要插入的数据
     * @return 插入数据后的list长度
     */
    Long listLeftPush(Object key, List<Object> dataList);

    /**
     * 将数组插入到列表的头部，从右侧插入
     * @param key key
     * @param dataList 需要插入的数据
     * @return 插入数据后的list长度
     */
    Long listRightPush(Object key, List<Object> dataList);

    /**
     * 在列表中index的位置设置value值
     * @param key 列表key
     * @param index 位置
     * @param value value值
     */
    void listSet(Object key, long index, Object value);

    /**
     * 从存储在键中的列表中删除等于值的元素的第一个计数事件
     * @param key 列表key
     * @param count count> 0：删除等于从头到尾移动的值的元素
     *              count <0：删除等于从尾到头移动的值的元素
     *              count = 0：删除等于value的所有元素
     * @param value 需要删除的值
     * @return 删除的数量
     */
    Long listRemove(Object key, long count, Object value);

    /**
     * 获取list指定位置的值
     * @param key 列表key
     * @param index 位置
     * @return 值
     */
    Object listIndex(Object key, long index);

    /**
     * 获取List指定范围的值
     * @param key 列表key
     * @param start 开始位置
     * @param end 结束位置，-1获取所有
     * @return 列表值
     */
    List<Object> listRange(Object key, long start, long end);

    /**
     * 删除给定的哈希hashKeys
     * @param key key
     * @param hashKeys hash key
     * @return 删除的数量
     */
    Long hashDelete(Object key, List<Object> hashKeys);

    /**
     * 确定哈希hashKey是否存在
     * @param key key
     * @param hashKey hashKey
     * @return 是否存在
     */
    Boolean hashHasKey(Object key, Object hashKey);

    /**
     * 从哈希获取给定hashKey的值
     * @param key key
     * @param hashKey hashKey
     * @return value
     */
    Object hashGet(Object key, Object hashKey);

    /**
     * 获取key所对应的散列表的key
     * @param key key
     * @return hash keys
     */
    Set<Object> hashKeys(Object key);

    /**
     * 获取key所对应的散列表的大小个数
     * @param key key
     * @return 长度
     */
    Long hashSize(Object key);

    /**
     * 添加多个hash
     * @param key key
     * @param dataMap map
     */
    void hashPutAll(Object key, Map<?, ?> dataMap);

    /**
     * key
     * @param key key
     * @param hashKey hashKey
     * @param value value
     */
    void hashPut(Object key, Object hashKey, Object value);

    /**
     * 获取hash所有值
     * @param key key
     * @return hash
     */
    Map<Object, Object> hashEntries(Object key);
}
