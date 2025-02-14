package com.smart.framework.ai.dify.api;

import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import com.smart.framework.ai.dify.api.request.ChatMessagesRequest;
import com.smart.framework.ai.dify.api.request.DifyConversationListRequest;
import com.smart.framework.ai.dify.api.request.DifyFileUploadRequest;
import com.smart.framework.ai.dify.api.request.DifyMessageHistoryRequest;
import com.smart.framework.ai.dify.api.response.*;
import reactor.core.publisher.Flux;

import java.util.List;

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
    Flux<AbstractChatCompletionResponse> chatMessagesStreaming(ChatMessagesRequest request);

    /**
     * 上传文件
     * @param request 文件上传请求
     * @return 文件上传结果
     */
    DifyFileUploadResponse uploadFile(DifyFileUploadRequest request);

    /**
     * 停止聊天消息
     * 仅支持流式模式。
     * @param taskId 任务 ID，可在流式返回 Chunk 中获取
     * @param user Required 用户标识，用于定义终端用户的身份，必须和发送消息接口传入 user 保持一致。
     * @return 是否停止成功
     */
    boolean stopChatMessage(String taskId, String user);

    /**
     * 获取会话历史消息
     * @param request 请求参数
     * @return 会话历史消息
     */
    List<DifyMessageHistoryResponse> messageHistory(DifyMessageHistoryRequest request);


    /**
     * 获取会话列表
     * @param request 请求参数
     * @return 会话列表
     */
    DifyConversationListResponse conversationList(DifyConversationListRequest request);

    /**
     * 删除会话
     * @param conversationId 会话 ID
     * @param user 用户标识，由开发者定义规则，需保证用户标识在应用内唯一。
     * @return 是否删除成功
     */
    boolean deleteConversation(String conversationId, String user);

    /**
     * 获取应用基本信息
     * @return 应用基本信息
     */
    DifyInfoResponse info();

    /**
     * 获取应用参数信息
     * 用于进入页面一开始，获取功能开关、输入参数名称、类型及默认值等使用
     * @return 应用参数信息
     */
    DifyParameterResponse parameters();

    /**
     * 获取应用Meta信息
     * @return 应用Meta信息
     */
    DifyMetaReponse meta();
}
