package com.smart.starter.cache.guava.data;

import lombok.*;

import java.time.Duration;
import java.time.Instant;

/**
 * @author shizhongming
 * 2020/9/11 9:46 下午
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CacheObject<T> {

    public CacheObject(T data, Duration timeout) {
        this(Instant.now(), data, timeout);
    }

    /**
     * 插入时间
     */
    private Instant operationTime;

    /**
     * 数据对象
     */
    private T data;

    /**
     * 超时时间
     */
    private Duration timeout;
}
