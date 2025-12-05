package com.smart.framework.extension.wechat.constants;

import com.smart.framework.commons.core.json.BaseEnum;
import lombok.Getter;

/**
 * @author zhongming4762
 * 2023/4/7
 */
@Getter
public enum WechatMsgTypeEnum implements BaseEnum<String> {
    /**
     * 消息
     */
    TEXT("text"),

    /**
     * 事件
     */
    EVENT("event");

    private final String value;

     WechatMsgTypeEnum(String value) {
        this.value = value;
    }
}
