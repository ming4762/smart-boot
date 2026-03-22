package com.smart.framework.auth.extensions.wechat.provider;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.core.wechat.WechatAuthConfigProvider;
import com.smart.framework.commons.core.utils.ApplicationContextUtils;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * 默认的微信授权配置提供者实现
 * 从
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-10 16:04
 * @since 5.0.0
 */
public class DefaultWechatAuthConfigProviderImpl implements WechatAuthConfigProvider {

    /**
     * 获取小程序配置
     *
     * @param authType authType
     * @return WechatConfig
     */
    @Override
    public String getDefaultAppid(AuthTypeEnum authType) {
        if (AuthTypeEnum.WECHAT_MP_QRCODE.equals(authType)) {
            if (this.wxMpService() == null) {
                return null;
            }
            return wxMpService().getWxMpConfigStorage().getAppId();
        }
        if (AuthTypeEnum.WECHAT_MINIAPP.equals(authType)) {
            if (this.wxMaService() == null) {
                return null;
            }
            return wxMaService().getWxMaConfig().getAppid();
        }
        return null;
    }

    private WxMpService wxMpService() {
        return ApplicationContextUtils.getBean(WxMpService.class);
    }

    private WxMaService wxMaService() {
        return ApplicationContextUtils.getBean(WxMaService.class);
    }
}
