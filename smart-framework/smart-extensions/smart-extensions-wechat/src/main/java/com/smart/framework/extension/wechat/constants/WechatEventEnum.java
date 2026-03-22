package com.smart.framework.extension.wechat.constants;

import com.smart.framework.commons.core.json.BaseEnum;
import lombok.Getter;

/**
 * @author zhongming4762
 * 2023/4/7
 */
@Getter
public enum WechatEventEnum implements BaseEnum<String> {
    /**
     * 关注
     */
    SUBSCRIBE("subscribe"),
    /**
     * 取消关注
     */
    UNSUBSCRIBE("unsubscribe"),

    SCAN("scan");

    private final String value;

    WechatEventEnum(String value) {
        this.value = value;
    }
}
