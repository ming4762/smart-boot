package com.smart.framework.ai.dify.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.framework.ai.dify.config.ClientConfig;
import com.smart.framework.ai.dify.constants.UrlEnum;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.commons.core.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * DIFY通用API
 * @author shizhongming
 * 2025/2/15 15:52
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class CommonApi {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final ClientConfig clientConfig;

    protected <T> T doRequest(String url, HttpMethod httpMethod, String contentType, Object parameters, ParameterizedTypeReference<T> typeReference) {
        return RestUtils.rest(url, httpMethod, createHeaders(contentType), parameters, typeReference, null);
    }

    protected <T> Flux<T> doStreamRequest(String url, String contentType, HttpMethod httpMethod, Object parameters, ParameterizedTypeReference<T> typeReference) {
        return RestUtils.restReactive(url, httpMethod, createHeaders(contentType), parameters, typeReference, null);
    }

    /**
     * 创建请求头
     * @return 请求头
     */
    protected Map<String, String> createHeaders(String contentType) {
        return Map.of(
                HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + clientConfig.getApiKey(),
                HttpHeaders.CONTENT_TYPE, contentType
        );
    }

    /**
     * 获取api地址
     * @param url url
     * @return api地址
     */
    protected String getApiUrl(UrlEnum url) {
        return clientConfig.getApiUrl() + url.getUrl();
    }


    /***
     * 创建用户参数
     * @param user 用户
     * @return 用户参数
     */
    protected Map<String, String> createUserParameter(String user) {
        return Map.of("user", user);
    }

    protected String createParameterUrl(UrlEnum url, Object parameter) {
        String pathParameter = JsonUtils.parse(JsonUtils.toJsonString(parameter), new TypeReference<Map<String, Object>>() {
                }).entrySet().stream()
                .filter(item -> item.getValue() != null)
                .map(item -> String.format("%s=%s", item.getKey(), item.getValue()))
                .collect(Collectors.joining("&"));

        return this.getApiUrl(url) + "?" + pathParameter;
    }
}
