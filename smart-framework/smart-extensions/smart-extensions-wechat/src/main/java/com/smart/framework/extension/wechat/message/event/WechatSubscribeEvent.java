package com.smart.framework.extension.wechat.message.event;

import com.smart.framework.extension.wechat.message.dto.WechatMessageResultDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 公众号关注事件
 * @author zhongming4762
 * 2023/4/7
 */
@Getter
@Setter
public class WechatSubscribeEvent extends ApplicationEvent {

    private WechatMessageResultDTO message;

    public WechatSubscribeEvent(Object source) {
        super(source);
    }
}
