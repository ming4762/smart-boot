package com.smart.framework.cache.guava.limit;

import com.google.common.util.concurrent.RateLimiter;
import com.smart.framework.commons.core.lock.limit.RateLimitService;
import org.jspecify.annotations.NonNull;

import java.time.temporal.ChronoUnit;
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
    private static final Map<String, RateLimitConfig> CONFIG_MAP = new ConcurrentHashMap<>();

    @Override
    public boolean acquire(@NonNull String key, long limit, @NonNull ChronoUnit unit) {
        RateLimiter limiter = getOrUpdateLimiter(key, limit, unit);
        return limiter.tryAcquire();
    }

    private RateLimiter getOrUpdateLimiter(String key, long limit, ChronoUnit unit) {
        RateLimitConfig newConfig = new RateLimitConfig(limit, unit);

        // 如果配置没变，直接返回已有 RateLimiter
        RateLimitConfig oldConfig = CONFIG_MAP.get(key);
        if (newConfig.equals(oldConfig)) {
            return RATE_LIMITER_MAP.get(key);
        }

        // 配置变更，重新初始化 RateLimiter
        synchronized (this) {
            oldConfig = CONFIG_MAP.get(key);
            if (!newConfig.equals(oldConfig)) {
                double permitsPerSecond = calculatePermitsPerSecond(limit, unit);
                RateLimiter newLimiter = RateLimiter.create(permitsPerSecond);
                RATE_LIMITER_MAP.put(key, newLimiter);
                CONFIG_MAP.put(key, newConfig);
                return newLimiter;
            }
        }
        return RATE_LIMITER_MAP.get(key);
    }

    private double calculatePermitsPerSecond(long limit, ChronoUnit unit) {
        long seconds = unit.getDuration().getSeconds();
        if (seconds <= 0) {
            throw new IllegalArgumentException("Unsupported unit: " + unit);
        }
        return (double) limit / seconds;
    }

    private record RateLimitConfig(long limit, ChronoUnit unit) {
    }
}
