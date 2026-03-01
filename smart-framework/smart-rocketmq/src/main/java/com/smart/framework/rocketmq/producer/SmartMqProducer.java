package com.smart.framework.rocketmq.producer;

import com.smart.framework.rocketmq.model.SmartMqMessage;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.jspecify.annotations.NonNull;

import java.time.Duration;

/**
 * MQ生产者
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026/3/1 01:19
 * @since 5.0.0
 */
public interface SmartMqProducer {

    /**
     * 发送消息
     * @param destination destination
     * @param message 消息
     * @return 是否发送成功
     */
    SendResult syncSend(@NonNull String destination, @NonNull SmartMqMessage<?> message);

    /**
     * 异步发送消息
     * @param destination destination
     * @param message 消息
     * @param sendCallback 回调
     */
    void asyncSend(@NonNull String destination, @NonNull SmartMqMessage<?> message, @NonNull SendCallback sendCallback);

    /**
     * 发送延迟消息
     * @param destination destination
     * @param message 消息
     * @param delayTime 延迟时间，单位毫秒
     * @return 是否发送成功
     */
    SendResult syncSendDelay(@NonNull String destination, @NonNull SmartMqMessage<?> message, @NonNull Duration delayTime);

    /**
     * 异步发送延迟消息
     * @param destination destination
     * @param message 消息
     * @param hashKey 路由 key，相同 key 保证顺序（通常用业务 ID）
     * @param sendCallback 回调
     */
    void asyncSendDelay(@NonNull String destination, @NonNull SmartMqMessage<?> message, @NonNull String hashKey, @NonNull SendCallback sendCallback);

    /**
     * 发送有序消息
     * @param destination destination
     * @param message 消息
     * @param hashKey 路由 key，相同 key 保证顺序（通常用业务 ID）
     * @return 是否发送成功
     */
    SendResult syncSendOrderly(@NonNull String destination, @NonNull SmartMqMessage<?> message, @NonNull String hashKey);

    /**
     * 异步发送有序消息
     * @param destination destination
     * @param message 消息
     * @param hashKey 路由 key，相同 key 保证顺序（通常用业务 ID）
     * @param sendCallback 回调
     */
    void asyncSendOrderly(@NonNull String destination, @NonNull SmartMqMessage<?> message, @NonNull String hashKey, @NonNull SendCallback sendCallback);
}
