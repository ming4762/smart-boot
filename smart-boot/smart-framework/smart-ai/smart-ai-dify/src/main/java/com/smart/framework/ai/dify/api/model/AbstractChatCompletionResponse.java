package com.smart.framework.ai.dify.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.framework.ai.dify.constants.ChatResponseEventTypeEnum;
import com.smart.framework.ai.dify.json.EnumValueJson;
import lombok.Getter;
import lombok.Setter;

/**
 * 返回 App 输出的流式块
 * @author shizhongming
 * 2025/2/8 21:58
 * @since 5.0.0
 */
@Getter
@Setter
public abstract class AbstractChatCompletionResponse {

    @JsonSerialize(using = EnumValueJson.EnumValueSerializer.class)
    @JsonDeserialize(using = EnumValueJson.EnumValueDeserializer.class)
    private ChatResponseEventTypeEnum event;

    /**
     * 任务 ID，用于请求跟踪和下方的停止响应接口
     */
    @JsonProperty("task_id")
    private String taskId;

    /**
     * 消息唯一 ID
     */
    @JsonProperty("message_id")
    private String messageId;

    /**
     * 会话 ID
     */
    @JsonProperty("conversation_id")
    private String conversationId;
}
