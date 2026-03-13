package com.smart.framework.extension.wechat.event.message;

import com.smart.framework.commons.core.event.SmartCommonEvent;
import com.smart.framework.extension.wechat.pojo.dto.WechatMessageResultDTO;
import lombok.Getter;

/**
 * 微信通用事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-28 18:07
 * @since 5.0.0
 */
@Getter
public abstract class AbstractWechatMessageEvent extends SmartCommonEvent {

    private final WechatMessageResultDTO message;

    protected AbstractWechatMessageEvent(Object source, WechatMessageResultDTO message) {
        super(source);
        this.message = message;
    }
}
