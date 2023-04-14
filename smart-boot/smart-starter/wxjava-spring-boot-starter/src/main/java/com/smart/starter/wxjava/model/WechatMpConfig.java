package com.smart.starter.wxjava.model;

import lombok.*;

import java.io.Serializable;

/**
 * 微信公众号配置信息
 * @author zhongming4762
 * 2023/4/7
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WechatMpConfig implements Serializable {

    /**
     * 设置微信公众号的appid
     */
    private String appid;

    /**
     * 设置微信公众号的Secret
     */
    private String secret;

    private String token;

    private String aesKey;
}
