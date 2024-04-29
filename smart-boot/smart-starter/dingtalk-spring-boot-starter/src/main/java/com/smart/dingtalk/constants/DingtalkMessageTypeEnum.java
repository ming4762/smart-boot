package com.smart.dingtalk.constants;

import lombok.Getter;

/**
 * 消息类型
 * @author shizhongming
 * 2024/4/28 17:34
 * @since 3.0.0
 */
@Getter
public enum DingtalkMessageTypeEnum {
    /**
     * 消息类型
     */
    TEXT("text", "文本消息"),
    IMAGE("image", "图片消息"),
    VOICE("voice", "语音消息"),
    FILE("file", "文件消息"),
    LINK("link", "链接消息"),
    OA("oa", "OA消息"),
    MARKDOWN("markdown", "markdown消息"),
    ACTION_CARD("action_card", "卡片消息")
    ;

    private final String type;

    private final String remark;

    private DingtalkMessageTypeEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }
}
