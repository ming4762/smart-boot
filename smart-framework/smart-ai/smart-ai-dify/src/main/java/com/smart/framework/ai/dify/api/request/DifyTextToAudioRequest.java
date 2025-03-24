package com.smart.framework.ai.dify.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 文字转语音请求
 * @author shizhongming
 * 2025/2/15 1:22
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyTextToAudioRequest {

    /**
     * Dify 生成的文本消息，那么直接传递生成的message-id 即可，后台会通过 message_id 查找相应的内容直接合成语音信息。如果同时传 message_id 和 text，优先使用 message_id。
     */
    @JsonProperty("message_id")
    private String messageId;

    /**
     * 语音生成内容。如果没有传 message-id的话，则会使用这个字段的内容
     */
    private String text;

    /**
     * 用户标识，由开发者定义规则，需保证用户标识在应用内唯一。
     */
    private String user;
}
