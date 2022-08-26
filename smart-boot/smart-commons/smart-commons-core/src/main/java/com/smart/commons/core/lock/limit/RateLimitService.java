package com.smart.commons.core.lock.limit;

import org.springframework.lang.NonNull;

/**
 * 限流服务类
 * @author ShiZhongMing
 * 2022/8/25
 * @since 3.0.0
 */
public interface RateLimitService {

    /**
     * 判断是否限流
     * @param key 限流的key
     * @param limit 每秒访问次数
     * @return true：未限制，false限制
     */
    boolean acquire(@NonNull String key, long limit);
}
