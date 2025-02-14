package com.smart.framework.ai.dify.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import com.smart.framework.ai.dify.api.request.*;
import com.smart.framework.ai.dify.api.response.*;
import com.smart.framework.ai.dify.config.ClientConfig;
import com.smart.framework.ai.dify.constants.ChatResponseEventTypeEnum;
import com.smart.framework.ai.dify.constants.ResponseModeEnum;
import com.smart.framework.ai.dify.constants.UrlEnum;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.commons.core.utils.RestUtils;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * dify客户端
 * @author shizhongming
 * 2025/2/8 20:16
 * @since 5.0.0
 */
@Setter
@AllArgsConstructor
public class DefaultDifyClient implements DifyClient {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final ClientConfig clientConfig;

    /**
     * 发送对话消息, 阻塞模式
     *
     * @param request 参数
     * @return 响应
     */
    @Override
    public ChatCompletionResponse chatMessagesBlocking(ChatMessagesRequest request) {
        ChatMessagesApiParameter parameter = new ChatMessagesApiParameter();
        BeanUtils.copyProperties(request, parameter);
        parameter.setResponseMode(ResponseModeEnum.BLOCKING);
        return this.doRequest(
                this.getApiUrl(UrlEnum.CHAT_MESSAGES),
                UrlEnum.CHAT_MESSAGES.getHttpMethod(),
                MediaType.APPLICATION_JSON_VALUE,
                parameter,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    /**
     * 发送对话消息, 流式模式
     *
     * @param request 参数
     * @return 响应
     */
    @Override
    public Flux<AbstractChatCompletionResponse> chatMessagesStreaming(ChatMessagesRequest request) {
        ChatMessagesApiParameter parameter = new ChatMessagesApiParameter();
        BeanUtils.copyProperties(request, parameter);
        parameter.setResponseMode(ResponseModeEnum.STREAMING);

        return this.doStreamRequest(
                this.getApiUrl(UrlEnum.CHAT_MESSAGES),
                MediaType.APPLICATION_JSON_VALUE,
                UrlEnum.CHAT_MESSAGES.getHttpMethod(),
                parameter,
                new ParameterizedTypeReference<String>() {
                }
        ).mapNotNull(item -> {
            Map<String, Object> data = JsonUtils.parse(item, new TypeReference<>() {
            });
            String eventType = (String) data.get("event");
            ChatResponseEventTypeEnum eventTypeEnum = ChatResponseEventTypeEnum.getByValue(eventType);
            if (eventTypeEnum == null) {
                return null;
            }
            AbstractChatCompletionResponse completionResponse = JsonUtils.parse(item, eventTypeEnum.getModelClass());
            completionResponse.setEvent(eventTypeEnum);
            return completionResponse;
        }).filter(item -> item != null && !ChatResponseEventTypeEnum.PING.equals(item.getEvent()));
    }

    /**
     * 上传文件
     *
     * @param request 文件上传请求
     * @return 文件上传结果
     */
    @Override
    public DifyFileUploadResponse uploadFile(DifyFileUploadRequest request) {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", request.getFile().getResource());
        builder.part("user", request.getUser());

        return RestUtils.restForm(
                this.getApiUrl(UrlEnum.FILES_UPLOAD),
                HttpMethod.POST,
                this.createHeaders(MediaType.MULTIPART_FORM_DATA_VALUE),
                builder.build(),
                new ParameterizedTypeReference<>() {
                },
                null
        );
    }

    /**
     * 停止聊天消息
     * 仅支持流式模式。
     *
     * @param taskId 任务 ID，可在流式返回 Chunk 中获取
     * @param user   Required 用户标识，用于定义终端用户的身份，必须和发送消息接口传入 user 保持一致。
     * @return 是否停止成功
     */
    @Override
    public boolean stopChatMessage(String taskId, String user) {
        String url = String.format(this.getApiUrl(UrlEnum.STOP_CHAT), taskId);
        DifyCommonResponse result = this.doRequest(
                url,
                UrlEnum.STOP_CHAT.getHttpMethod(),
                MediaType.APPLICATION_JSON_VALUE,
                this.createUserParameter(user),
                new ParameterizedTypeReference<>() {
                }
        );
        return result != null && result.isSuccess();
    }

    /**
     * 获取会话历史消息
     *
     * @param request 请求参数
     * @return 会话历史消息
     */
    @Override
    public List<DifyMessageHistoryResponse> messageHistory(DifyMessageHistoryRequest request) {
        return this.doRequest(
                this.createParameterUrl(UrlEnum.MESSAGES_HISTORY, request),
                UrlEnum.MESSAGES_HISTORY.getHttpMethod(),
                MediaType.APPLICATION_JSON_VALUE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    /**
     * 获取会话列表
     *
     * @param request 请求参数
     * @return 会话列表
     */
    @Override
    public DifyConversationListResponse conversationList(DifyConversationListRequest request) {
        return this.doRequest(
                this.createParameterUrl(UrlEnum.CONVERSATIONS, request),
                UrlEnum.CONVERSATIONS.getHttpMethod(),
                MediaType.APPLICATION_JSON_VALUE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    /**
     * 删除会话
     *
     * @param conversationId 会话 ID
     * @param user 用户标识，由开发者定义规则，需保证用户标识在应用内唯一。
     * @return 是否删除成功
     */
    @Override
    public boolean deleteConversation(String conversationId, String user) {
        String url = String.format(this.getApiUrl(UrlEnum.DELETE_CONVERSATIONS), conversationId);
        DifyCommonResponse commonResponse = this.doRequest(
                url,
                UrlEnum.DELETE_CONVERSATIONS.getHttpMethod(),
                MediaType.APPLICATION_JSON_VALUE,
                this.createUserParameter(user),
                new ParameterizedTypeReference<>() {
                }
        );
        return commonResponse != null && commonResponse.isSuccess();
    }

    /**
     * 获取应用基本信息
     *
     * @return 应用基本信息
     */
    @Override
    public DifyInfoResponse info() {
        return this.doRequest(
                this.getApiUrl(UrlEnum.INFO),
                UrlEnum.INFO.getHttpMethod(),
                MediaType.APPLICATION_JSON_VALUE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    /**
     * 获取应用参数信息
     * 用于进入页面一开始，获取功能开关、输入参数名称、类型及默认值等使用
     *
     * @return 应用参数信息
     */
    @Override
    public DifyParameterResponse parameters() {
        return this.doRequest(
                this.getApiUrl(UrlEnum.PARAMETERS),
                UrlEnum.PARAMETERS.getHttpMethod(),
                MediaType.APPLICATION_JSON_VALUE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    /**
     * 获取应用Meta信息
     *
     * @return 应用Meta信息
     */
    @Override
    public DifyMetaReponse meta() {
        return this.doRequest(
                this.getApiUrl(UrlEnum.META),
                UrlEnum.META.getHttpMethod(),
                MediaType.APPLICATION_JSON_VALUE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    private <T> T doRequest(String url, HttpMethod httpMethod, String contentType, Object parameters, ParameterizedTypeReference<T> typeReference) {
        return RestUtils.rest(url, httpMethod, createHeaders(contentType), parameters, typeReference, null);
    }

    private <T> Flux<T> doStreamRequest(String url, String contentType, HttpMethod httpMethod, Object parameters, ParameterizedTypeReference<T> typeReference) {
        return RestUtils.restReactive(url, httpMethod, createHeaders(contentType), parameters, typeReference, null);
    }

    /**
     * 获取api地址
     * @param url url
     * @return api地址
     */
    private String getApiUrl(UrlEnum url) {
        return clientConfig.getApiUrl() + url.getUrl();
    }

    /**
     * 创建请求头
     * @return 请求头
     */
    private Map<String, String> createHeaders(String contentType) {
        return Map.of(
                HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + clientConfig.getApiKey(),
                HttpHeaders.CONTENT_TYPE, contentType
        );
    }

    /***
     * 创建用户参数
     * @param user 用户
     * @return 用户参数
     */
    private Map<String, String> createUserParameter(String user) {
        return Map.of("user", user);
    }

    private String createParameterUrl(UrlEnum url, Object parameter) {
        String pathParameter = JsonUtils.parse(JsonUtils.toJsonString(parameter), new TypeReference<Map<String, Object>>() {
                }).entrySet().stream()
                .filter(item -> item.getValue() != null)
                .map(item -> String.format("%s=%s", item.getKey(), item.getValue()))
                .collect(Collectors.joining("&"));

        return this.getApiUrl(url) + "?" + pathParameter;
    }
}
