package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import com.smart.framework.ai.dify.api.model.Metadata;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息结束事件，收到此事件则代表流式返回结束
 * @author shizhongming
 * 2025/2/8 22:10
 * @since 5.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponseMessageEndEvent extends AbstractChatCompletionResponse {

    /**
     * 元数据
     */
    private Metadata metadata;
}
