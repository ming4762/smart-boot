package com.smart.cloud.starter.feign.exception;

import lombok.Getter;

/**
 * 业务异常
 * 解决统一异常拦截后导致的无法降级的问题
 * @author shizhongming
 * 2025/6/11 20:49
 * @since 5.0.0
 */
@Getter
public class SmartFeignBusinessException extends RuntimeException {

    private final Object errorData;


    public SmartFeignBusinessException(Object errorData) {
        this.errorData = errorData;
    }

    public SmartFeignBusinessException(String message, Object errorData) {
        super(message);
        this.errorData = errorData;
    }
}
