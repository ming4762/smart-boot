package com.smart.commons.core.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author shizhongming
 * 2021/3/21 8:38 上午
 */
public class RestUtils {

    private RestUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static RestTemplate restTemplate;


    public static <T> ResponseEntity<T> rest(@NonNull String url, @NonNull HttpMethod httpMethod, Map<String, String> headers, Object parameter, @NonNull Class<T> clazz, Object ...uriVariables) {
        final HttpHeaders httpHeaders = new HttpHeaders();

        headers.forEach(httpHeaders :: add);
        final HttpEntity<?> httpEntity = new HttpEntity<>(parameter, httpHeaders);

        return  restTemplate.exchange(url, httpMethod, httpEntity, clazz, uriVariables);
    }

    /**
     * 设置 RestTemplate
     * @param restTemplate RestTemplate
     */
    public static void setRestTemplate(RestTemplate restTemplate) {
        RestUtils.restTemplate = restTemplate;
    }
}
