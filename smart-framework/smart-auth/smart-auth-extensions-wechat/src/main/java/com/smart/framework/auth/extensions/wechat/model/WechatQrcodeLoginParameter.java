package com.smart.framework.auth.extensions.wechat.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 微信服务号扫码登录参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 18:55
 * @since 5.0.0
 */
@Getter
@Setter
public class WechatQrcodeLoginParameter implements Serializable {

    /**
     * 微信服务号扫码登录场景值
     */
    private String scene;
}
