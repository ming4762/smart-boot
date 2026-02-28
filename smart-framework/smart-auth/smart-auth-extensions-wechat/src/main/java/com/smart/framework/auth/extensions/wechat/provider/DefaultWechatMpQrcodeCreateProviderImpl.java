package com.smart.framework.auth.extensions.wechat.provider;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.core.wechat.WechatAuthConfigProvider;
import com.smart.framework.auth.extensions.wechat.model.WechatMpQrcodeResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpQrcodeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * 默认微信公众号二维码创建提供器
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-27 16:38
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class DefaultWechatMpQrcodeCreateProviderImpl implements WechatMpQrcodeCreateProvider {

    private static final String SCENE = "MP_QRCODE_LOGIN";

    private final WxMpService wxMpService;
    private final WechatAuthConfigProvider wechatAuthConfigProvider;

    /**
     * 生成ORCODE
     *
     * @param appid appid
     * @return ORCODE
     */
    @SneakyThrows({WxErrorException.class})
    @Override
    public WechatMpQrcodeResult createQrcode(String appid) {
        if (appid == null) {
            appid = this.wechatAuthConfigProvider.getDefaultAppid(AuthTypeEnum.WECHAT_MP_QRCODE);
        }
        if (StringUtils.hasText(appid)) {
            this.wxMpService.switchoverTo(appid);
        }
        WxMpQrcodeService qrcodeService = this.wxMpService.getQrcodeService();
        long expireSeconds = Duration.ofMinutes(20).getSeconds();
        WxMpQrCodeTicket qrCodeTicket = qrcodeService.qrCodeCreateTmpTicket(SCENE, (int) expireSeconds);
        String qrcodeUrl = qrcodeService.qrCodePictureUrl(qrCodeTicket.getTicket());
        return WechatMpQrcodeResult.builder()
                .ticket(qrCodeTicket.getTicket())
                .url(qrcodeUrl)
                .scene(SCENE)
                .expireSeconds(expireSeconds)
                .build();
    }
}
