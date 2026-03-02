package com.smart.framework.auth.extensions.wechat.provider;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.extensions.wechat.model.WechatLoginResult;

/**
 * 微信扫码服务号扫码登录
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 19:16
 * @since 5.0.0
 */
public class WechatMpQrcodeLoginProvider implements WechatLoginProvider {
    /**
     * 微信登录
     *
     * @param appid appid
     * @param credentials  credentials
     * @return 登录结果
     */
    @Override
    public WechatLoginResult login(String appid, Object credentials) {
        String openid = (String) credentials;
        WechatLoginResult result = new WechatLoginResult();
        result.setOpenid(openid);
        return result;
    }

    /**
     * 获取支持的登录类型
     *
     * @return WechatLoginTypeEnum
     */
    @Override
    public AuthTypeEnum supportLoginType() {
        return AuthTypeEnum.WECHAT_MP_QRCODE;
    }
}
