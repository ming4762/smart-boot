package com.smart.starter.redis.service;

import com.smart.commons.core.lock.limit.RateLimitService;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.springframework.lang.NonNull;

import java.time.Duration;

/**
 * @author ShiZhongMing
 * 2022/8/26 9:34
 * @since 1.0 connection.eval(LUA_SCRIPT.getBytes(StandardCharsets.UTF_8), ReturnType.MULTI, )
 */
public class RedisRateLimitServiceImpl implements RateLimitService {

    private final RedisService redisService;

    public RedisRateLimitServiceImpl(RedisService  redisService) {
        this.redisService = redisService;
    }

    @Override
    public boolean acquire(@NonNull String key, long limit) {
        RRateLimiter rateLimiter = this.redisService.getRedissonClient().getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, limit, Duration.ofSeconds(1));
        return rateLimiter.tryAcquire();
    }
}
