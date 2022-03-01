package com.smart.starter.redis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Reids info数据
 * @author ShiZhongMing
 * 2022/2/28
 * @since 2.0.0
 */
@Getter
@AllArgsConstructor
public class RedisInfo {

    private final String group;

    private final String key;

    private final String value;

    private final String description;
}
