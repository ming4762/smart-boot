package com.smart.framework.message.wechat.mq;

import com.smart.framework.extension.wechat.event.message.AbstractWechatMessageEvent;
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

    /**
     * 监听微信消息并发送到MQ
     * @param event 微信消息事件
     */
    @EventListener(AbstractWechatMessageEvent.class)
    public void send(AbstractWechatMessageEvent event) {

    }
}
