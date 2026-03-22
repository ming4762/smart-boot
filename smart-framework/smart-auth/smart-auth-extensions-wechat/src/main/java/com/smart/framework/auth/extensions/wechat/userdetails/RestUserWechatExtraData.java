package com.smart.framework.auth.extensions.wechat.userdetails;

import lombok.*;

import java.io.Serializable;

/**
 * 微信登录用户额外信息
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 22:43
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestUserWechatExtraData implements Serializable {

    private String appid;

    private String openid;

    private String unionid;

}
