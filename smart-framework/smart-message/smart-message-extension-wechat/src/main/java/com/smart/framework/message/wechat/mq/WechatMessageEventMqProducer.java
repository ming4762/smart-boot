package com.smart.framework.message.wechat.mq;

import com.smart.framework.extension.wechat.event.message.AbstractWechatMessageEvent;
import com.smart.framework.rocketmq.constants.SmartMqDestinationConstants;
import com.smart.framework.rocketmq.model.SmartMqMessage;
import com.smart.framework.rocketmq.producer.SmartMqProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

/**
 * 微信消息事件MQ生产者
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-28 19:32
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class WechatMessageEventMqProducer {

    private final SmartMqProducer smartMqProducer;

    /**
     * 监听微信消息并发送到MQ
     * @param event 微信消息事件
     */
    @EventListener(AbstractWechatMessageEvent.class)
    public void send(AbstractWechatMessageEvent event) {
        this.smartMqProducer.syncSend(
                String.join(":", SmartMqDestinationConstants.WECHAT_EVENT, SmartMqDestinationConstants.WECHAT_EVENT_MESSAGE_TAG),
                SmartMqMessage.builder()
                        .payload(event.getMessage())
                        .build()
        );
    }
}
