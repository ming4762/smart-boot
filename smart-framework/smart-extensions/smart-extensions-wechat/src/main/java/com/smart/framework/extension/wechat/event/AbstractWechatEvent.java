package com.smart.framework.extension.wechat.event;

import org.springframework.context.ApplicationEvent;

/**
 * 微信通用事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-28 18:07
 * @since 5.0.0
 */
public abstract class AbstractWechatEvent extends ApplicationEvent {

    public AbstractWechatEvent(Object source) {
        super(source);
    }
}
