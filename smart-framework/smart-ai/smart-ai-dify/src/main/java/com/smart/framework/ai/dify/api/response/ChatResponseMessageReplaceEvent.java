package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息内容替换事件。 开启内容审查和审查输出内容时，若命中了审查条件，则会通过此事件替换消息内容为预设回复。
 * @author shizhongming
 * 2025/2/8 22:10
 * @since 5.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponseMessageReplaceEvent extends AbstractChatCompletionResponse {

    /**
     * 替换内容（直接替换 LLM 所有回复文本）
     */
    private String answer;

    /**
     * 消息创建时间戳
     */
    @JsonProperty("created_at")
    private Long createdAt;
}
