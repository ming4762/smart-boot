package com.smart.framework.extension.wechat.message.event;

import com.smart.framework.extension.wechat.message.dto.WechatMessageResultDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 微信通用事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-27 23:07
 * @since 5.0.0
 */
@Getter
@Setter
public class WechatCommonEvent  extends ApplicationEvent {

    private WechatMessageResultDTO message;

    public WechatCommonEvent(Object source) {
        super(source);
    }
}
