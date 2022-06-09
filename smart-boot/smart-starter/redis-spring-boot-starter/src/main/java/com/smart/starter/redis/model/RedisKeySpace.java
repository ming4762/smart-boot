package com.smart.starter.redis.model;

import lombok.Getter;

/**
 * redis key Space
 *
 * @author ShiZhongMing
 * 2022/2/25
 * @since 2.0.0
 */
@Getter
public record RedisKeySpace(String db, Long keyNum, Long canExpireKeyNum, Long avgTtl) {

}
