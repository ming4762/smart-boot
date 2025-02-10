package com.smart.framework.ai.dify.api;

import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import com.smart.framework.ai.dify.api.request.ChatMessagesRequest;
import com.smart.framework.ai.dify.api.response.ChatCompletionResponse;
import com.smart.framework.ai.dify.api.response.DifyInfoResponse;
import reactor.core.publisher.Flux;

/**
 * dify客户端
 * @author shizhongming
 * 2025/2/8 19:32
 * @since 5.0.0
 */
public interface DifyClient {

    /**
     * 发送对话消息, 阻塞模式
     * @param request 参数
     * @return 响应
     */
    ChatCompletionResponse chatMessagesBlocking(ChatMessagesRequest request);

    /**
     * 发送对话消息, 流式模式
     * @param request 参数
     * @return 响应
     */
    Flux<? extends AbstractChatCompletionResponse> chatMessagesStreaming(ChatMessagesRequest request);


    /**
     * 获取应用基本信息
     * @return 应用基本信息
     */
    DifyInfoResponse info();
}
