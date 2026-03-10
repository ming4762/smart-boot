package com.smart.framework.auth.extensions.wechat.provider;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.core.wechat.WechatAuthConfigProvider;
import com.smart.framework.auth.extensions.wechat.cache.WechatMpQrcodeCacheData;
import com.smart.framework.auth.extensions.wechat.model.WechatMpQrcodeResult;
import com.smart.framework.commons.core.utils.SmartIdGenerator;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpQrcodeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.jspecify.annotations.Nullable;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * 默认微信公众号二维码创建提供器
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-27 16:38
 * @since 5.0.0
 */
public class DefaultWechatMpQrcodeCreateProviderImpl implements WechatMpQrcodeCreateProvider {

    private static final String SCENE = "MP_QRCODE_LOGIN";

    private final WxMpService wxMpService;
    private final AuthCache authCache;
    @Nullable
    private final WechatAuthConfigProvider wechatAuthConfigProvider;

    public DefaultWechatMpQrcodeCreateProviderImpl(WxMpService wxMpService, AuthCache authCache, @Nullable WechatAuthConfigProvider wechatAuthConfigProvider) {
        this.wxMpService = wxMpService;
        this.authCache = authCache;
        this.wechatAuthConfigProvider = wechatAuthConfigProvider;
    }

    /**
     * 生成ORCODE
     *
     * @param appId appId
     * @return ORCODE
     */
    @SneakyThrows({WxErrorException.class})
    @Override
    public WechatMpQrcodeResult createQrcode(String appId) {
        if (appId == null && this.wechatAuthConfigProvider != null) {
            appId = this.wechatAuthConfigProvider.getDefaultAppid(AuthTypeEnum.WECHAT_MP_QRCODE);
        }
        Assert.notNull(appId, "appId must not be null");
        this.wxMpService.switchoverTo(appId);

        WxMpQrcodeService qrcodeService = this.wxMpService.getQrcodeService();
        Duration duration = Duration.ofMinutes(20);
        long expireSeconds = duration.getSeconds();
        String scene = SCENE + SmartIdGenerator.nextId();
        // 缓存二维码场景值，用于后续校验
        this.authCache.put(scene, new WechatMpQrcodeCacheData(appId), duration);
        // 生成二维码
        WxMpQrCodeTicket qrCodeTicket = qrcodeService.qrCodeCreateTmpTicket(scene, (int) expireSeconds);
        String qrcodeUrl = qrcodeService.qrCodePictureUrl(qrCodeTicket.getTicket());
        return WechatMpQrcodeResult.builder()
                .url(qrcodeUrl)
                .scene(scene)
                .expireSeconds(expireSeconds)
                .build();
    }
}
