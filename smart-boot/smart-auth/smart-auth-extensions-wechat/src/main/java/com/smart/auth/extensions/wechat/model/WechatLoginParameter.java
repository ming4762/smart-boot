package com.smart.auth.extensions.wechat.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 微信登录参数
 * @author zhongming4762
 * 2023/4/3
 */
@Getter
@Setter
public class WechatLoginParameter implements Serializable {

    private String code;
}
