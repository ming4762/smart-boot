package com.smart.cloud.starter.feign.codec;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.cloud.starter.feign.exception.SmartFeignBusinessException;
import com.smart.framework.commons.core.utils.JsonUtils;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

/**
 * 业务错误解码器
 * 解决统一异常拦截后导致的无法降级的问题
 * @author shizhongming
 * 2025/6/11 10:54
 * @since 5.0.0
 */
public class BusinessDecoder extends ResponseEntityDecoder {

    private static final String CODE_KEY = "code";
//    private static final String SUCCESS_KEY = "success";
    private static final String DATA_KEY = "data";
    private static final String MESSAGE_KEY = "message";
    private static final String ERROR_CODE = "500";

    public BusinessDecoder(Decoder decoder) {
        super(decoder);
    }

//    @Override
//    public Object decode(Response response, Type type) throws IOException, FeignException {
//        // 判断是否是application/json类型，如果不是则调用父类的decode方法
//        Collection<String> contentTypes = response.headers().get(HttpHeaders.CONTENT_TYPE);
//        String contentType = contentTypes != null && !contentTypes.isEmpty() ? contentTypes.iterator().next() : "";
//        if (!contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
//            return super.decode(response, type);
//        }
//
//        String string = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
//        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
//        JsonNode root = objectMapper.readTree(string);
//        if (root.isArray()) {
//            JavaType javaType = objectMapper.getTypeFactory().constructType(type);
//            return objectMapper.convertValue(root, javaType);
//        }
//        if (root.isObject() && root.has(CODE_KEY)) {
//            // 检查响应是否成功
//            boolean success = true;
//            if (root.has(SUCCESS_KEY)) {
//                success = root.get(SUCCESS_KEY).asBoolean(true);
//            }
//
//            if (!success) {
//                // 业务异常，从统一响应格式中提取错误信息
//                Map<?,?> err = objectMapper.convertValue(root, Map.class);
//                String errorMessage = err.get(DATA_KEY) == null ? (String) err.get(MESSAGE_KEY) : JsonUtils.toJsonString(err.get(DATA_KEY));
//                throw new SmartFeignBusinessException(errorMessage, err);
//            }
//        }
//
//        JavaType javaType = objectMapper.getTypeFactory().constructType(type);
//        return objectMapper.convertValue(root, javaType);
//    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        // 判断是否是application/json类型，如果不是则调用父类的decode方法
        Collection<String> contentTypes = response.headers().get(HttpHeaders.CONTENT_TYPE);
        String contentType = contentTypes != null && !contentTypes.isEmpty() ? contentTypes.iterator().next() : "";
        if (!contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return super.decode(response, type);
        }

        String string = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
        JsonNode root = objectMapper.readTree(string);
        if (root.isArray()) {
            JavaType javaType = objectMapper.getTypeFactory().constructType(type);
            return objectMapper.convertValue(root, javaType);
        }
        if (root.isObject() && root.has(CODE_KEY)) {
            String code = root.get(CODE_KEY).asText();
            if (ERROR_CODE.equals(code)) {
                // 系统异常
                Map<?,?> err = objectMapper.convertValue(root, Map.class);
                throw new SmartFeignBusinessException(err.get(DATA_KEY) == null ? (String) err.get(MESSAGE_KEY) : JsonUtils.toJsonString(err.get(DATA_KEY)), err);
            }
        }

        JavaType javaType = objectMapper.getTypeFactory().constructType(type);
        return objectMapper.convertValue(root, javaType);
    }
}
