package com.smart.framework.rocketmq.event;

import com.smart.framework.commons.core.constants.SmartEventTypeEnum;
import com.smart.framework.commons.core.event.SmartCommonEvent;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.rocketmq.model.SmartMqMessage;
import com.smart.framework.rocketmq.producer.SmartMqProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;

/**
 * Spring事件生产者，将spring事件发送到MQ
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-12 23:13
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class SmartSpringEventProducer {

    private final String eventDestination;
    private final SmartMqProducer smartMqProducer;

    @Value("${spring.application.name}")
    private String applicationName;

    @EventListener(SmartCommonEvent.class)
    public void send(SmartCommonEvent event) {
        if (!event.isLocal()) {
            // 非本地消息，不处理，防止消息重复消费
            return;
        }
        // 标记为远程消息并发送到stream
        event.setEventType(SmartEventTypeEnum.REMOTE);
        // 包装消息
        SmartEventWrapper eventWrapper = SmartEventWrapper.builder()
                .eventClass(event.getClass().getName())
                .eventData(JsonUtils.toJsonString(event))
                .eventSourceService(applicationName)
                .build();
        smartMqProducer.syncSend(
                eventDestination,
                SmartMqMessage.builder()
                        .payload(eventWrapper)
                        .build()
        );
    }
}
