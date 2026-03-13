package com.smart.framework.rocketmq.event;

import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.rocketmq.consumer.SmartBaseConsumer;
import com.smart.framework.rocketmq.model.SmartMqMessage;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 接收MQ事件并转为spring事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-12 23:20
 * @since 5.0.0
 */
@RocketMQMessageListener(
        topic = "${smart.mq.eventTopic:smart-boot-event}",
        selectorExpression = "${smart.mq.eventTag:event}",
        consumerGroup = "${spring.application.name}"
)
public class SmartSpringEventConsumer implements SmartBaseConsumer<SmartEventWrapper>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 业务消息处理，子类实现
     *
     * @param message 消息对象
     */
    @Override
    public void doOnMessage(SmartMqMessage<SmartEventWrapper> message) {
        SmartEventWrapper eventWrapper = message.getPayload();
        if (applicationName.equals(eventWrapper.getEventSourceService())) {
            // 自身消息不处理
            return;
        }
        String eventClass = eventWrapper.getEventClass();
        Class<?> aClass = this.getClass(eventClass);
        if (aClass != null) {
            applicationContext.publishEvent(JsonUtils.parse(eventWrapper.getEventData(), aClass));
        } else {
            applicationContext.publishEvent(JsonUtils.parse(eventWrapper.getEventData()));
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
