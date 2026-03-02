package com.smart.framework.auth.extensions.wechat.monitor;

import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.extension.wechat.event.message.AbstractWechatMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

/**
 * 微信登录扫描Spring事件监控
 * 用于单体应用场景，监听微信登录扫码事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 11:09
 * @since 5.0.0
 */
@Slf4j
public class WechatLoginScanSpringEventMonitor extends AbstractWechatLoginScanMonitor {

    public WechatLoginScanSpringEventMonitor(AuthCache authCache) {
        super(authCache);
    }

    @EventListener(AbstractWechatMessageEvent.class)
    public void onMessage(AbstractWechatMessageEvent event) {
        this.handleScanResult(event.getMessage());
    }
}
