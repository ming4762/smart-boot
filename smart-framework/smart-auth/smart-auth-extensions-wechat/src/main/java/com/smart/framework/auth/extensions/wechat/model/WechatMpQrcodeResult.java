package com.smart.framework.auth.extensions.wechat.model;

import lombok.*;

import java.io.Serializable;

/**
 * 微信公众号二维码生成结果
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-27 17:13
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WechatMpQrcodeResult implements Serializable {

    private String url;

    private String scene;

    private Long expireSeconds;
}
