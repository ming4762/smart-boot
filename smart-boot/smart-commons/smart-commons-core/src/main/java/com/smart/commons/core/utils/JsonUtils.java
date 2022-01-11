package com.smart.commons.core.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * json 工具类
 * @author jackson
 * 2020/2/15 8:48 下午
 */
@Slf4j
public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 对象转为json
     * @param object 实体
     * @return json字符串
     */
    @SneakyThrows
    public static String toJsonString(Object object) {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    /**
     * json转为对象
     * @param json json字符串
     * @return 实体
     */
    @SneakyThrows
    public static Object parse(String json) {
        return OBJECT_MAPPER.readValue(json, Object.class);
    }

    /**
     * json转为对象
     * @param json json字符串
     * @return 实体
     */
    @SneakyThrows
    public static <T>  T parse(String json, Class<T> clazz) {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    /**
     * 转换list
     * @param json json
     * @param clazz 实体类类型
     * @param <T> T
     * @return List列表
     */
    @SneakyThrows
    public static <T> List<T> parseCollection(String json, Class<T> clazz) {
        final JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
        return OBJECT_MAPPER.readValue(json, javaType);
    }
}
