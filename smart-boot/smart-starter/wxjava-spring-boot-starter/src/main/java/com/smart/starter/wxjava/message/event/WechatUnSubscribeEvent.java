package com.smart.starter.wxjava.message.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 公众号取消关注事件
 * @author zhongming4762
 * 2023/4/7
 */
@Getter
@Setter
public class WechatUnSubscribeEvent extends ApplicationEvent {

    private String toUserName;

    private String fromUserName;

    private String createTime;

    private String eventKey;

    public WechatUnSubscribeEvent(Object source) {
        super(source);
    }
}
