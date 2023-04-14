package com.smart.starter.wxjava.message.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 验证微信消息dto
 * @author zhongming4762
 * 2023/4/7
 */
@Getter
@Setter
@ToString
public class WechatMessageCheckDTO implements Serializable {

    private String signature;

    private String nonce;

    private String timestamp;

    private String echostr;
}
