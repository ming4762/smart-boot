package com.smart.module.api.message.constants;

import lombok.Getter;

/**
 * 消息通道列表
 * @author zhongming4762
 * 2023/5/31
 */
@Getter
public enum MessageChannelEnum {

    /**
     * 消息发送通道
     */
    SYSTEM("SYSTEM", "系统消息"),
    SMS("SMS", "短信"),
    EMAIL("EMAIL", "邮件消息"),
    WECHAT("WECHAT", "微信消息"),
    DINGDING("DINGDING", "钉钉消息"),
    WEB_SOCKET("WEB_SOCKET", "web socket消息")
    ;

    private final String value;

    private final String remark;

    MessageChannelEnum(String value, String remark) {
        this.value = value;
        this.remark = remark;
    }
}
