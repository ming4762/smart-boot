package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * 流式输出过程中出现的异常会以 stream event 形式输出，收到异常事件后即结束。
 * @author shizhongming
 * 2025/2/8 22:10
 * @since 5.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponseErrorEvent extends AbstractChatCompletionResponse {

    /**
     * HTTP 状态码
     */
    private Integer status;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误信息
     */
    private String message;
}
