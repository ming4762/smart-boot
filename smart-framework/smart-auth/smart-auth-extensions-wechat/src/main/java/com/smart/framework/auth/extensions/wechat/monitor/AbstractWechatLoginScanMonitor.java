package com.smart.framework.auth.extensions.wechat.monitor;

import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.extensions.wechat.cache.WechatMpQrcodeCacheData;
import com.smart.framework.extension.wechat.pojo.dto.WechatMessageResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * 微信登录扫码监控器抽象类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 18:06
 * @since 5.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class AbstractWechatLoginScanMonitor {

    private final AuthCache authCache;

    protected void handleScanResult(WechatMessageResultDTO wechatScanResult) {
        // 获取scene
        String scene = wechatScanResult.getEventKey();
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
        cacheData.setOpenId(wechatScanResult.getFromUserName());
        cacheData.setMpId(wechatScanResult.getToUserName());
        // 重新设置缓存，有效期60秒
        this.authCache.put(scene, cacheData, Duration.ofSeconds(60));
    }
}
