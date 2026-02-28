package com.smart.framework.auth.extensions.wechat.provider;

import com.smart.framework.auth.extensions.wechat.model.WechatMpQrcodeResult;

/**
 * 生成服务号二维码
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026/2/27 16:34
 * @since 5.0.0
 */
public interface WechatMpQrcodeCreateProvider {

    /**
     * 生成ORCODE
     * @param appid appid
     * @return ORCODE
     */
    WechatMpQrcodeResult createQrcode(String appid);
}
