package com.smart.starter.redis.service;

import com.smart.commons.core.lock.limit.RateLimitService;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/8/26 9:34
 * @since 1.0 connection.eval(LUA_SCRIPT.getBytes(StandardCharsets.UTF_8), ReturnType.MULTI, )
 */
public class RedisRateLimitServiceImpl implements RateLimitService {

    private static final String LUA_SCRIPT = "return redis.call('cl.throttle',KEYS[1], ARGV[1], ARGV[2], ARGV[3])";

    private final RedisService redisService;

    public RedisRateLimitServiceImpl(RedisService  redisService) {
        this.redisService = redisService;
    }

    @Override
    public boolean acquire(@NonNull String key, long limit) {
        var limitStr = Long.valueOf(limit).toString();
        List<Long> result = this.redisService.getRedisTemplate()
                .execute((RedisCallback<List<Long>>) connection -> connection.scriptingCommands().eval(LUA_SCRIPT.getBytes(StandardCharsets.UTF_8), ReturnType.MULTI, 1,
                        key.getBytes(StandardCharsets.UTF_8), limitStr.getBytes(StandardCharsets.UTF_8), limitStr.getBytes(StandardCharsets.UTF_8), "1".getBytes(StandardCharsets.UTF_8)));
        if (CollectionUtils.isEmpty(result)) {
            return false;
        }
        return result.get(0).intValue() == 0;
    }
}
