package com.smart.framework.commons.core.utils;

import com.smart.framework.commons.core.exception.SystemException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.jspecify.annotations.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

/**
 * @author shizhongming
 * 2021/3/21 8:38 上午
 */
public class RestUtils {

    private RestUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static WebClient webClient;

    private static final Function<ClientResponse, Mono<? extends Throwable>> ERROR_HANDLER = response -> response.bodyToMono(String.class)
            .flatMap(errorBody -> Mono.error(new SystemException("服务器错误: " + errorBody)));

    /**
     * 发送请求
     * @param url URL
     * @param httpMethod 请求方式
     * @param headers 请求头
     * @param parameter 参数
     * @param typeReference 返回类型
     * @param uriVariables URL参数
     * @return 请求结果
     * @param <T> 泛型
     */
    public static <T> T rest(@NonNull String url, @NonNull HttpMethod httpMethod, Map<String, String> headers, Object parameter, @NonNull ParameterizedTypeReference<T> typeReference, Map<String, ?> uriVariables) {
        if (uriVariables == null) {
            uriVariables = Map.of();
        }
        return webClient.method(httpMethod)
                .uri(url, uriVariables)
                .bodyValue(Objects.requireNonNullElse(parameter, ""))
                .headers(httpHeaders -> {
                    if (!CollectionUtils.isEmpty(headers)) {
                        headers.forEach(httpHeaders::add);
                    }
                }).retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, ERROR_HANDLER)
                .bodyToMono(typeReference)
                .block();
    }

    /**
     * 发送响应性请求
     * @param url URL
     * @param httpMethod 请求方式
     * @param headers 请求头
     * @param parameter 参数
     * @param typeReference 返回类型
     * @return 请求结果
     * @param <T> 泛型
     */
    public static <T> Flux<T> restReactive(@NonNull String url, @NonNull HttpMethod httpMethod, Map<String, String> headers, Object parameter, @NonNull ParameterizedTypeReference<T> typeReference, Map<String, ?> uriVariables) {
        if (uriVariables == null) {
            uriVariables = Map.of();
        }
        return webClient.method(httpMethod)
                .uri(url, uriVariables)
                .headers(httpHeaders -> {
                    if (!CollectionUtils.isEmpty(headers)) {
                        headers.forEach(httpHeaders::add);
                    }
                }).bodyValue(Objects.requireNonNullElse(parameter, ""))
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, ERROR_HANDLER)
                .bodyToFlux(typeReference);
    }

    /**
     * 发送form请求
     * @param url URL
     * @param httpMethod 请求方式
     * @param headers 请求头
     * @param parameter 参数
     * @param typeReference 返回类型
     * @param uriVariables URL参数
     * @return 请求结果
     * @param <T> 泛型
     */
    public static <T> T restForm(@NonNull String url, @NonNull HttpMethod httpMethod, Map<String, String> headers, MultiValueMap<String, ?> parameter, @NonNull ParameterizedTypeReference<T> typeReference, Map<String, ?> uriVariables) {
        if (uriVariables == null) {
            uriVariables = Map.of();
        }
        MultiValueMap<String, ?> nonNullParameter = Objects.requireNonNullElseGet(parameter, () -> MultiValueMap.fromSingleValue(Map.of()));
        return webClient.method(httpMethod)
                .uri(url, uriVariables)
                .headers(httpHeaders -> {
                    if (!CollectionUtils.isEmpty(headers)) {
                        headers.forEach(httpHeaders::add);
                    }
                    if (httpHeaders.getContentType() == null) {
                        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                    }
                }).body(BodyInserters.fromMultipartData(nonNullParameter))
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, ERROR_HANDLER)
                .bodyToMono(typeReference)
                .block();
    }


    /**
     * 下载文件
     * @param url 文件下载URL
     * @param httpMethod 请求方式
     * @param headers 请求头
     * @param outputStream 输出流
     * @param uriVariables 参数
     */
    public static CountDownLatch download(@NonNull String url, @NonNull HttpMethod httpMethod, Map<String, String> headers, Object parameter, OutputStream outputStream, Map<String, ?> uriVariables) {
        CountDownLatch latch = new CountDownLatch(1);
        if (uriVariables == null) {
            uriVariables = Map.of();
        }
        Flux<DataBuffer> dataBufferFlux = webClient.method(httpMethod)
                .uri(url, uriVariables)
                .headers(httpHeaders -> {
                    if (!CollectionUtils.isEmpty(headers)) {
                        headers.forEach(httpHeaders::add);
                    }
                })
                .bodyValue(Objects.requireNonNullElse(parameter, ""))
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, ERROR_HANDLER)
                .bodyToFlux(DataBuffer.class);

        DataBufferUtils.write(dataBufferFlux, outputStream)
                .doOnNext(DataBufferUtils::release)
                .doOnTerminate(latch::countDown)
                .subscribe();

        return latch;
    }

    /**
     * 设置 WebClient
     * @param webClient WebClient
     */
    public static void setWebClient(WebClient webClient) {
        RestUtils.webClient = webClient;
    }

    /**
     * 获取 WebClient
     * @return WebClient
     */
    public static WebClient getWebClient() {
        return webClient;
    }

}
