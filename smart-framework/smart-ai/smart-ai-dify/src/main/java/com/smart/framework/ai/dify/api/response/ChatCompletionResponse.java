package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.framework.ai.dify.api.model.Metadata;
import lombok.Getter;
import lombok.Setter;

/**
 * 当 response_mode 为 blocking 时，返回 ChatCompletionResponse object。
 * @author shizhongming
 * 2025/2/8 19:41
 * @since 5.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatCompletionResponse {

    /**
     * (string) 消息唯一 ID
     */
    @JsonProperty("message_id")
    private String messageId;

    /**
     *  (string) 会话 ID
     */
    @JsonProperty("conversation_id")
    private String conversationId;

    /**
     * App 模式，固定为 chat
     */
    private String mode;

    /**
     * 完整回复内容
     */
    private String answer;

    /**
     * 元数据
     */
    private Metadata metadata;

    /**
     * 消息创建时间戳（秒）
     */
    @JsonProperty("created_at")
    private Long createdAt;
}
