package com.smart.starter.redis.model;

import lombok.Getter;

/**
 * Reids info数据
 *
 * @author ShiZhongMing
 * 2022/2/28
 * @since 2.0.0
 */
@Getter
public record RedisInfo(String group, String key, String value, String description) {

}
