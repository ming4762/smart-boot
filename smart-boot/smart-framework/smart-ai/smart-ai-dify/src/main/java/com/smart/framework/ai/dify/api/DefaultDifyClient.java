package com.smart.framework.ai.dify.api;

import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import com.smart.framework.ai.dify.api.request.ChatMessagesApiParameter;
import com.smart.framework.ai.dify.api.request.ChatMessagesRequest;
import com.smart.framework.ai.dify.api.response.ChatCompletionResponse;
import com.smart.framework.ai.dify.api.response.DifyInfoResponse;
import com.smart.framework.ai.dify.config.ClientConfig;
import com.smart.framework.ai.dify.constants.ChatResponseEventTypeEnum;
import com.smart.framework.ai.dify.constants.ResponseModeEnum;
import com.smart.framework.ai.dify.constants.UrlEnum;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.commons.core.utils.RestUtils;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.util.Map;

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
                HttpMethod.POST,
                MediaType.APPLICATION_JSON_VALUE,
                parameter,
                ChatCompletionResponse.class
        );
    }

    /**
     * 发送对话消息, 流式模式
     *
     * @param request 参数
     * @return 响应
     */
    @Override
    public Flux<? extends AbstractChatCompletionResponse> chatMessagesStreaming(ChatMessagesRequest request) {
        ChatMessagesApiParameter parameter = new ChatMessagesApiParameter();
        BeanUtils.copyProperties(request, parameter);
        parameter.setResponseMode(ResponseModeEnum.STREAMING);

        return this.doStreamRequest(
                this.getApiUrl(UrlEnum.CHAT_MESSAGES),
                HttpMethod.POST,
                MediaType.APPLICATION_JSON_VALUE,
                parameter,
                String.class
        ).mapNotNull(item -> {
            Map<String, Object> data = JsonUtils.parse(item, Map.class);
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
     * 获取应用基本信息
     *
     * @return 应用基本信息
     */
    @Override
    public DifyInfoResponse info() {
        return this.doRequest(
                this.getApiUrl(UrlEnum.INFO),
                HttpMethod.GET,
                MediaType.APPLICATION_JSON_VALUE,
                null,
                DifyInfoResponse.class
        );
    }

    private <T> T doRequest(String url, HttpMethod httpMethod, String contentType, Object parameters, Class<T> responseType) {
        ResponseEntity<T> response = RestUtils.rest(url, httpMethod, createHeaders(contentType), parameters, responseType);
        // todo: 判断响应码
        return response.getBody();
    }

    private <T> Flux<T> doStreamRequest(String url, HttpMethod httpMethod, String contentType, Object parameters, Class<T> responseType) {
        return RestUtils.restStream(url, httpMethod, createHeaders(contentType), parameters, responseType);
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
}
