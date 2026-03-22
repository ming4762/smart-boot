package com.smart.framework.extension.wechat.event.message;

import com.smart.framework.extension.wechat.pojo.dto.WechatMessageResultDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信消息事件
 * @author zhongming4762
 * 2023/4/7
 */
@Getter
@Setter
public class WechatMessageTextEvent extends AbstractWechatMessageEvent {

    public WechatMessageTextEvent(Object source, WechatMessageResultDTO message) {
        super(source, message);
    }
}
