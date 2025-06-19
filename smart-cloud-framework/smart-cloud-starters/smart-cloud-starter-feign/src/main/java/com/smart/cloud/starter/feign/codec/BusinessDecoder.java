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

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
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
    private static final String SUCCESS_CODE = "200";
    private static final String DATA_KEY = "data";

    public BusinessDecoder(Decoder decoder) {
        super(decoder);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        String string = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
        JsonNode root = objectMapper.readTree(string);
        if (root.isArray()) {
            JavaType javaType = objectMapper.getTypeFactory().constructType(type);
            return objectMapper.convertValue(root, javaType);
        }
        if (root.isObject() && root.has(CODE_KEY)) {
            String code = root.get(CODE_KEY).asText();
            if (!SUCCESS_CODE.equals(code)) {
                // 业务异常
                Map<?,?> err = objectMapper.convertValue(root, Map.class);
                throw new SmartFeignBusinessException(err.get(DATA_KEY) == null ? null : JsonUtils.toJsonString(err.get(DATA_KEY)), err);
            }
            JsonNode dataNode = root.get(DATA_KEY);
            if (dataNode == null || dataNode.isNull()) {
                return null;
            }
            JavaType javaType = objectMapper.getTypeFactory().constructType(type);
            return objectMapper.convertValue(dataNode, javaType);
        }

        JavaType javaType = objectMapper.getTypeFactory().constructType(type);
        return objectMapper.convertValue(root, javaType);
    }
}
