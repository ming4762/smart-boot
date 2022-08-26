package com.smart.starter.cache.guava.limit;

import com.google.common.util.concurrent.RateLimiter;
import com.smart.commons.core.lock.limit.RateLimitService;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于guava实现的限流器
 * @author ShiZhongMing
 * 2022/8/25
 * @since 3.0.0
 */
@SuppressWarnings("UnstableApiUsage")
public class GuavaRateLimitServiceImpl implements RateLimitService {

    private static final Map<String, RateLimiter> RATE_LIMITER_MAP = new ConcurrentHashMap<>();

    private static final String SPLIT = "@@";

    @Override
    public boolean acquire(@NonNull String key, long limit) {
        var limiterKey = key + SPLIT + limit;
        var limiter = this.getLimiter(limiterKey, limit);
        return limiter.tryAcquire();
    }

    /**
     * 获取 RateLimiter
     * @param key key
     * @return RateLimiter
     */
    protected RateLimiter getLimiter(String key, long limit) {
        RateLimiter limiter = RATE_LIMITER_MAP.get(key);
        if (limiter != null) {
            return limiter;
        }
        synchronized (this) {
            if (limiter == null) {
                limiter = RateLimiter.create(limit);
                RATE_LIMITER_MAP.put(key, limiter);
            }
        }
        return RATE_LIMITER_MAP.get(key);
    }
}
