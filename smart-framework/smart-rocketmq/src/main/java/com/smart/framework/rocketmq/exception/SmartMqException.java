package com.smart.framework.rocketmq.exception;

/**
 * MQ 异常类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 11:21
 * @since 5.0.0
 */
public class SmartMqException extends RuntimeException {

    public SmartMqException(String message) {
        super(message);
    }

    public SmartMqException(String message, Throwable cause) {
        super(message, cause);
    }
}
