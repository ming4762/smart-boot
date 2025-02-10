package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * Agent模式下返回文本块事件，即：在Agent模式下，文章的文本以分块的方式输出（仅Agent模式下使用）
 * @author shizhongming
 * 2025/2/8 22:11
 * @since 5.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponseAgentMessageEvent extends AbstractChatCompletionResponse {

    /**
     * LLM 返回文本块内容
     */
    private String answer;

    /**
     * 消息创建时间戳
     */
    @JsonProperty("created_at")
    private Long createdAt;
}
