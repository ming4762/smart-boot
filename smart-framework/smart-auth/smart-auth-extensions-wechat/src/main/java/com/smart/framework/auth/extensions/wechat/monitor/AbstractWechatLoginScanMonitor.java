package com.smart.framework.auth.extensions.wechat.monitor;

import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.extensions.wechat.cache.WechatMpQrcodeCacheData;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.extension.wechat.pojo.dto.WechatMessageResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        WechatMpQrcodeCacheData cacheData = this.authCache.getValue(scene);
        if (cacheData == null) {
            log.warn("微信服务号扫码登录失败，未查询到缓存信息，scanResult={}", JsonUtils.toJsonString(wechatScanResult));
            return;
        }
        cacheData.setValidated(true);
        cacheData.setOpenId(wechatScanResult.getFromUserName());
        // 重新设置缓存，有效期60秒
        this.authCache.put(scene, cacheData, Duration.ofSeconds(60));
    }
}
