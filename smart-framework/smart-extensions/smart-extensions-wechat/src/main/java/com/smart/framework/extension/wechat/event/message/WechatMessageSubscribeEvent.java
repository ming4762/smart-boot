package com.smart.framework.extension.wechat.event.message;

import com.smart.framework.extension.wechat.pojo.dto.WechatMessageResultDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * 公众号关注事件
 * @author zhongming4762
 * 2023/4/7
 */
@Getter
@Setter
public class WechatMessageSubscribeEvent extends AbstractWechatMessageEvent {

    public WechatMessageSubscribeEvent(Object source, WechatMessageResultDTO message) {
        super(source, message);
    }
}
