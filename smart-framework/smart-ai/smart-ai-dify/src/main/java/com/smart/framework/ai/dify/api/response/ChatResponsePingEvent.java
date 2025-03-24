package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * 每 10s 一次的 ping 事件，保持连接存活
 * @author shizhongming
 * 2025/2/8 22:10
 * @since 5.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponsePingEvent extends AbstractChatCompletionResponse {

}
