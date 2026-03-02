package com.smart.framework.rocketmq.consumer;

import com.fasterxml.jackson.databind.JavaType;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.rocketmq.exception.SmartMqException;
import com.smart.framework.rocketmq.model.SmartMqMessage;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;

import java.io.Serializable;

/**
 * MQ 消费者基类
 * 使用方继承此类，实现 handleMessage 方法即可
 * 封装了统一的日志、异常处理逻辑
 * 使用示例：
 * <pre>
 * {@code
 * @Component
 * @RocketMQMessageListener(
 *     topic = "${wx.mp.scan-login-topic:wx-scan-login}",
 *     consumerGroup = "${wx.mp.consumer-group:wx-scan-consumer}"
 * )
 * public class WxScanEventConsumer extends SmartBaseConsumer<WxScanEvent> {
 *     @Override
 *     protected void handleMessage(WxScanEvent message) {
 *         // 业务逻辑
 *     }
 * }
 * }
 * </pre>
 *
 * @param <T> 消息类型，必须继承 BaseMessage
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 11:16
 * @since 5.0.0
 */
public interface SmartBaseConsumer<T extends Serializable> extends RocketMQListener<String> {

    Logger LOGGER = LoggerFactory.getLogger(SmartBaseConsumer.class);

    @Override
    default void onMessage(String messageStr) {
        Class<T> payloadClass = this.getPayloadClass();
        JavaType javaType = JsonUtils.getObjectMapper()
                .getTypeFactory()
                .constructParametricType(SmartMqMessage.class, payloadClass);
        SmartMqMessage<T> message = JsonUtils.parse(messageStr, javaType);
        LOGGER.info("[MQ] 收到消息 messageId={} source={} class={}",
                message.getMessageId(), message.getSource(), payloadClass.getName());

        try {
            // 幂等校验（子类可重写）
            if (isDuplicate(message)) {
                LOGGER.warn("[MQ] 重复消息，跳过处理 messageId={}", message.getMessageId());
                return;
            }
            doOnMessage(message);
            LOGGER.debug("[MQ] 消息处理完成 messageId={}", message.getMessageId());
        } catch (Exception e) {
            LOGGER.error("[MQ] 消息处理异常 messageId={}", message.getMessageId(), e);
            // 抛出异常触发 RocketMQ 重试
            throw new SmartMqException("消息处理失败", e);
        }
    }

    /**
     * 业务消息处理，子类实现
     * @param message 消息对象
     */
    void doOnMessage(SmartMqMessage<T> message);

    /**
     * 幂等判断，子类按需重写
     * 默认不做幂等，依赖业务自身保证
     *
     * @return true 表示重复消息，跳过处理
     */
    default boolean isDuplicate(SmartMqMessage<T> message) {
        return false;
    }

    /**
     * 获取消息负载类型
     * @return 消息负载类型
     */
    default Class<T> getPayloadClass() {
        return (Class<T>) GenericTypeResolver.resolveTypeArguments(this.getClass(), SmartBaseConsumer.class)[0];
    }
}
