package com.smart.starter.redis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * redis key Space
 * @author ShiZhongMing
 * 2022/2/25
 * @since 2.0.0
 */
@Getter
@AllArgsConstructor
public class RedisKeySpace {

    private final String db;

    private final Long keyNum;

    private final Long canExpireKeyNum;

    private final Long avgTtl;
}
