package com.smart.framework.auth.extensions.wechat.listener;

import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.extensions.wechat.cache.WechatMpQrcodeCacheData;
import com.smart.framework.extension.wechat.event.message.AbstractWechatMessageEvent;
import com.smart.framework.extension.wechat.pojo.dto.WechatMessageResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * 微信登录扫描Spring事件监控
 * 用于单体应用场景，监听微信登录扫码事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 11:09
 * @since 5.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class WechatLoginScanSpringEventListener {

    private final AuthCache authCache;

    @EventListener(AbstractWechatMessageEvent.class)
    public void onMessage(AbstractWechatMessageEvent event) {
        WechatMessageResultDTO message = event.getMessage();

        // 获取scene
        String scene = message.getEventKey();
        if (!StringUtils.hasText(scene)) {
            // 不是扫码登录消息
            return;
        }
        WechatMpQrcodeCacheData cacheData = this.authCache.getValue(scene);
        if (cacheData == null) {
            // 不是扫码登录消息
            return;
        }
        cacheData.setValidated(true);
        cacheData.setOpenId(message.getFromUserName());
        cacheData.setMpId(message.getToUserName());
        // 重新设置缓存，有效期60秒
        this.authCache.put(scene, cacheData, Duration.ofSeconds(60));
    }
}
