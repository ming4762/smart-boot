package com.smart.framework.rocketmq.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

/**
 * RocketMQ消息模型
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 01:15
 * @since 5.0.0
 */
@Builder
@Getter
public class SmartMqMessage<T extends Serializable> implements Serializable {

    @Builder.Default
    private String messageId = UUID.randomUUID().toString();

    @Builder.Default
    private long timestamp = System.currentTimeMillis();

    /**
     * 消息来源
     */
    private String source;

    /**
     * 消息负载
     */
    private T payload;
}
