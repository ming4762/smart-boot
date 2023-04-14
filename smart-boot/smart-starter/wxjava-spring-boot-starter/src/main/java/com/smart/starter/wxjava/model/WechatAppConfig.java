package com.smart.starter.wxjava.model;

import lombok.*;

import java.io.Serializable;

/**
 * 微信小程序配置
 * @author zhongming4762
 * 2023/4/4
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WechatAppConfig implements Serializable {

    /**
     * 设置微信小程序的appid
     */
    private String appid;

    /**
     * 设置微信小程序的Secret
     */
    private String secret;

    /**
     * 设置微信小程序消息服务器配置的token
     */
    private String token;

    /**
     * 设置微信小程序消息服务器配置的EncodingAESKey
     */
    private String aesKey;

    /**
     * 消息格式，XML或者JSON
     */
    private String msgDataFormat;
}
