package com.smart.monitor.actuator.redis.constants;

import lombok.Getter;

/**
 * Redis 指标
 * @author ShiZhongMing
 * 2022/2/25
 * @since 2.0.0
 */
@Getter
public enum RedisMetricsConstants {

    /**
     * redis key
     */
    keys("redis.keys", "Redis key数量")
    ;

    private final String meterName;

    private final String description;

    RedisMetricsConstants(String meterName, String description) {
        this.meterName = meterName;
        this.description = description;
    }
}
