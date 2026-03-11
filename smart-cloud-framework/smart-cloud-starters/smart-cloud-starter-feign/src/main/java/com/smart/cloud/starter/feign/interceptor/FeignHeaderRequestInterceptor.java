package com.smart.cloud.starter.feign.interceptor;

import com.smart.cloud.starter.feign.holder.ServerWebExchangeContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ServerWebExchange;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Feign请求头拦截器，用于在Feign请求中透传请求头
 * @author shizhongming
 * 2025/10/11 10:19
 * @since 5.0.0
 */
public class FeignHeaderRequestInterceptor implements RequestInterceptor {

    private static final List<String> REQUEST_HEADER_NAMES = Arrays.asList(
            HttpHeaders.AUTHORIZATION,
            HttpHeaders.ACCEPT_LANGUAGE
    );

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, String> headers = this.getHeaders();
        headers.forEach(requestTemplate::header);
    }

    /**
     * 获取Feign请求头
     * @return Feign请求头
     */
    private Map<String, String> getHeaders() {
        Map<String, String> headers = this.getWebServletHeaders();
        if (!CollectionUtils.isEmpty(headers)) {
            return headers;
        }
        headers = this.getServerWebExchangeHeaders();
        if (!CollectionUtils.isEmpty(headers)) {
            return headers;
        }
        return HashMap.newHashMap(0);
    }

    private Map<String, String> getServerWebExchangeHeaders() {
        ServerWebExchange exchange = ServerWebExchangeContextHolder.getExchange();
        if (exchange == null) {
            return Collections.emptyMap();
        }
        HttpHeaders headers = exchange.getRequest().getHeaders();
        return REQUEST_HEADER_NAMES.stream()
                .map(item -> {
                    String headerValue = headers.getFirst(item);
                    if (!StringUtils.hasText(headerValue)) {
                        return null;
                    }
                    return Map.entry(item, headerValue);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 获取Web Servlet请求头
     * @return Web Servlet请求头
     */
    private Map<String, String> getWebServletHeaders() {
        HttpServletRequest request = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
        if (request == null) {
            return Collections.emptyMap();
        }
        return REQUEST_HEADER_NAMES.stream()
                .map(item -> {
                    String headerValue = request.getHeader(item);
                    if (!StringUtils.hasText(headerValue)) {
                        return null;
                    }
                    return Map.entry(item, headerValue);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
