package com.smart.framework.auth.extensions.wechat.cache;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 微信公众号二维码缓存数据
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 17:50
 * @since 5.0.0
 */
@Getter
@Setter
public class WechatMpQrcodeCacheData implements Serializable {

    /**
     * 是否扫码通过
     */
    private Boolean validated;

    /**
     * 微信用户OpenID
     */
    private String openId;

    /**
     * 微信公众号ID
     */
    private String mpId;

    /**
     * 微信服务号AppID
     */
    private String appid;

    public WechatMpQrcodeCacheData(String appid) {
        this.validated = false;
        this.appid = appid;
    }

    public boolean isValidated() {
        return Boolean.TRUE.equals(this.validated);
    }
}
