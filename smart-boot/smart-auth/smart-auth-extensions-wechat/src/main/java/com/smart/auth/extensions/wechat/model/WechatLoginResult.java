package com.smart.auth.extensions.wechat.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/4/3
 */
@Getter
@Setter
public class WechatLoginResult implements Serializable {

    private String openid;

    private String unionid;
}
