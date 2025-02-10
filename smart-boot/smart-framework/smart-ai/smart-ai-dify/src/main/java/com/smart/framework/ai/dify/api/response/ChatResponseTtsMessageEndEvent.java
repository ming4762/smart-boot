package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * TTS 音频流结束事件，收到这个事件表示音频流返回结束。
 * @author shizhongming
 * 2025/2/8 22:10
 * @since 5.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponseTtsMessageEndEvent extends AbstractChatCompletionResponse {

    /**
     * 结束事件是没有音频的，所以这里是空字符串
     */
    private String audio;

    /**
     * 消息创建时间戳
     */
    @JsonProperty("created_at")
    private Long createdAt;
}
