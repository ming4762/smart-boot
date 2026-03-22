package com.smart.framework.extension.wechat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 微信配置文件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 14:43
 * @since 5.0.0
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractWechatConfig implements Serializable {

    /**
     * 设置微信appid
     */
    private String appid;

    /**
     * 设置微信Secret
     */
    private String secret;

    /**
     * 设置微信服务器配置的token
     */
    private String token;

    /**
     * 设置微信配置的EncodingAESKey
     */
    private String aesKey;
}
