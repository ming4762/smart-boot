package com.smart.framework.auth.cache.redis.session;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.session.data.redis.RedisSessionRepository;

/**
 * @author shizhongming
 * 2025/3/12 17:09
 * @since 5.0.0
 */
public class SmartRedisSessionRepository extends RedisSessionRepository {
    /**
     * Create a new {@link RedisSessionRepository} instance.
     *
     * @param sessionRedisOperations the {@link RedisOperations} to use for managing
     *                               sessions
     */
    public SmartRedisSessionRepository(RedisOperations<String, Object> sessionRedisOperations) {
        super(sessionRedisOperations);
    }
}
