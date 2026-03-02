package com.smart.framework.rocketmq.producer;

import com.smart.framework.rocketmq.model.SmartMqMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * RocketMQ生产者实现类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 01:22
 * @since 5.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class SmartMqProducerRocketImpl implements SmartMqProducer {

    private final String destinationPrefix;
    private final RocketMQTemplate rocketMQTemplate;

    /**
     * 发送消息
     *
     * @param destination destination
     * @param message 消息
     * @return 是否发送成功
     */
    @Override
    public SendResult syncSend(@NonNull String destination, @NonNull SmartMqMessage<?> message) {
        return this.doSend(destination, message, () -> this.rocketMQTemplate.syncSend(this.getRealDestination(destination), message));
    }

    /**
     * 异步发送消息
     *
     * @param destination destination
     * @param message 消息
     */
    @Override
    public void asyncSend(@NonNull String destination, @NonNull SmartMqMessage<?> message, @NonNull SendCallback sendCallback) {
        this.doSend(destination, message, () -> {
            this.rocketMQTemplate.asyncSend(this.getRealDestination(destination), message, sendCallback);
            return null;
        });
    }

    /**
     * 发送延迟消息
     *
     * @param destination destination
     * @param message   消息
     * @param delayTime 延迟时间，单位毫秒
     * @return 是否发送成功
     */
    @Override
    public SendResult syncSendDelay(@NonNull String destination, @NonNull SmartMqMessage<?> message, @NonNull Duration delayTime) {
        return this.doSend(destination, message, () -> this.rocketMQTemplate.syncSendDelayTimeMills(this.getRealDestination(destination), message, delayTime.toMillis()));
    }

    /**
     * 异步发送延迟消息
     *
     * @param destination destination
     * @param message      消息
     * @param hashKey      路由 key，相同 key 保证顺序（通常用业务 ID）
     * @param sendCallback 回调
     */
    @Override
    public void asyncSendDelay(@NonNull String destination, @NonNull SmartMqMessage<?> message, @NonNull String hashKey, @NonNull SendCallback sendCallback) {
        this.doSend(destination, message, () -> {
            this.rocketMQTemplate.asyncSendOrderly(this.getRealDestination(destination), message, hashKey, sendCallback);
            return null;
        });
    }

    /**
     * 发送有序消息
     *
     * @param destination destination
     * @param message 消息
     * @param hashKey 路由 key，相同 key 保证顺序（通常用业务 ID）
     * @return 是否发送成功
     */
    @Override
    public SendResult syncSendOrderly(@NonNull String destination, @NonNull SmartMqMessage<?> message, @NonNull String hashKey) {
        return this.doSend(destination, message, () -> this.rocketMQTemplate.syncSendOrderly(this.getRealDestination(destination), message, hashKey));
    }

    /**
     * 异步发送有序消息
     *
     * @param destination destination
     * @param message      消息
     * @param hashKey      路由 key，相同 key 保证顺序（通常用业务 ID）
     * @param sendCallback 回调
     */
    @Override
    public void asyncSendOrderly(@NonNull String destination, @NonNull SmartMqMessage<?> message, @NonNull String hashKey, @NonNull SendCallback sendCallback) {
        this.doSend(destination, message, () -> {
            this.rocketMQTemplate.asyncSendOrderly(this.getRealDestination(destination), message, hashKey, sendCallback);
            return null;
        });
    }

    @Nullable
    protected SendResult doSend(@NonNull String destination, @NonNull SmartMqMessage<?> message, @NonNull Supplier<SendResult> supplier) {
        log.info("Sending message to destination: {}, businessMessageId: {}", destination, message.getMessageId());
        SendResult sendResult = supplier.get();
        if (sendResult != null) {
            log.info("Message sent successfully, mqMessageId: {}", sendResult.getMsgId());
        }
        return sendResult;
    }

    private String getRealDestination(@NonNull String destination) {
        return destinationPrefix + destination;
    }
}
