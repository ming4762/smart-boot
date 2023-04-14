package com.smart.auth.extensions.wechat.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信小程序登录返回结果
 * @author zhongming4762
 * 2023/4/3
 */
@Getter
@Setter
public class WechatAppLoginResult extends WechatLoginResult {

    private String sessionKey;
}
