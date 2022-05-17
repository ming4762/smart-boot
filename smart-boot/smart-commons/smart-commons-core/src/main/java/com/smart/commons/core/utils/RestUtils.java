package com.smart.commons.core.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(httpHeaders :: add);
        }
        final HttpEntity<?> httpEntity = new HttpEntity<>(parameter, httpHeaders);

        return restTemplate.exchange(url, httpMethod, httpEntity, clazz, uriVariables);
    }

    /**
     * 下载文件
     * @param url 文件下载URL
     * @param httpMethod 请求方式
     * @param headers 请求头
     * @param outputStream 输出流
     * @param uriVariables 参数
     */
    public static void download(@NonNull String url, @NonNull HttpMethod httpMethod, Map<String, String> headers, Object parameter, OutputStream outputStream, Object ...uriVariables) {
        RequestCallback requestCallback = request -> {
            request.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
            if (MapUtils.isNotEmpty(headers)) {
                headers.forEach((key, value) -> request.getHeaders().add(key, value));
            }
            if (parameter != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(parameter);
                objectOutputStream.flush();
                request.getBody().write(byteArrayOutputStream.toByteArray());
                objectOutputStream.close();
            }
        };
        restTemplate.execute(url, httpMethod, requestCallback, response -> {
            IOUtils.copy(response.getBody(), outputStream);
            return null;
        }, uriVariables);
    }

    /**
     * 设置 RestTemplate
     * @param restTemplate RestTemplate
     */
    public static void setRestTemplate(RestTemplate restTemplate) {
        RestUtils.restTemplate = restTemplate;
    }

}
