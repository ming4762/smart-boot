package com.smart.starter.wxjava.message.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 微信消息事件
 * @author zhongming4762
 * 2023/4/7
 */
@Getter
@Setter
public class WechatMessageEvent extends ApplicationEvent {

    private String toUserName;

    private String fromUserName;

    private String createTime;

    private String content;

    private String msgId;

    public WechatMessageEvent(Object source) {
        super(source);
    }
}
