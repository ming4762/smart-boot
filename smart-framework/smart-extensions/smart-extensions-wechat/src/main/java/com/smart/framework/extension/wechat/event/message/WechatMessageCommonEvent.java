package com.smart.framework.extension.wechat.event.message;

import com.smart.framework.extension.wechat.pojo.dto.WechatMessageResultDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信通用事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-27 23:07
 * @since 5.0.0
 */
@Getter
@Setter
public class WechatMessageCommonEvent extends AbstractWechatMessageEvent {

    public WechatMessageCommonEvent(Object source, WechatMessageResultDTO message) {
        super(source, message);
    }
}
