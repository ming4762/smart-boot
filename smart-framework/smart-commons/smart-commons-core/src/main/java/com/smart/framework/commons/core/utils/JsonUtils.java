package com.smart.framework.commons.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json 工具类
 * @author jackson
 * 2020/2/15 8:48 下午
 */
@Slf4j
public final class JsonUtils {

    private static final DateTimeFormatter ISO_8601_FORMATTER = new DateTimeFormatterBuilder().appendInstant(3).toFormatter();

    /**
     * 默认日期时间格式
     */
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认日期格式
     */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 默认时间格式
     */
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    private static ObjectMapper objectMapper;

    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));

        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));

        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        javaTimeModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer(ISO_8601_FORMATTER));

        // 禁用 WRITE_DATES_AS_TIMESTAMPS，确保序列化为 ISO-8601 格式
        DEFAULT_OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        DEFAULT_OBJECT_MAPPER.registerModule(javaTimeModule);
    }

    public static void initObjectMapper(ObjectMapper initObjectMapper) {
        objectMapper = initObjectMapper;
    }

    /**
     * 对象转为json
     * @param object 实体
     * @return json字符串
     */
    @SneakyThrows(JsonProcessingException.class)
    public static String toJsonString(Object object) {
        return getObjectMapper().writeValueAsString(object);
    }

    /**
     * json转为对象
     * @param json json字符串
     * @return 实体
     */
    @SneakyThrows(JsonProcessingException.class)
    public static Object parse(String json) {
        return getObjectMapper().readValue(json, Object.class);
    }

    /**
     * json转为对象
     * @param json json字符串
     * @return 实体
     */
    @SneakyThrows(JsonProcessingException.class)
    public static <T>  T parse(String json, Class<T> clazz) {
        return getObjectMapper().readValue(json, clazz);
    }

    /**
     * 转换list
     * @param json json
     * @param clazz 实体类类型
     * @param <T> T
     * @return List列表
     */
    @SneakyThrows(JsonProcessingException.class)
    public static <T> List<T> parseCollection(String json, Class<T> clazz) {
        final JavaType javaType = getObjectMapper().getTypeFactory().constructCollectionType(List.class, clazz);
        return getObjectMapper().readValue(json, javaType);
    }

    /**
     * json转想要的对象；应对各种复杂格式的对象很万能。
     *
     * @param json          json字符串
     * @param typeReference 泛型对象
     * @param <T> 类型
     * @return 实体
     */
    @SneakyThrows(JsonProcessingException.class)
    public static <T> T parse(String json, TypeReference<T> typeReference) {
        return getObjectMapper().readValue(json, typeReference);
    }

    /**
     * 将json压扁转为map
     * @param json json
     * @return map
     */
    @SneakyThrows({JsonProcessingException.class})
    public static Map<String, Object> flattenJson(String json) {
        if (!StringUtils.hasText(json)) {
            return Map.of();
        }
        JsonNode jsonNode = getObjectMapper().readTree(json);
        return flattenJson(jsonNode, "");
    }

    /**
     * 将json压扁转为map
     * @param jsonNode jsonNode
     * @param parentKey 父级key
     * @return map
     */
    public static Map<String, Object> flattenJson(JsonNode jsonNode, String parentKey) {
        Map<String, Object> flattenedMap = HashMap.newHashMap(8);

        if (!jsonNode.isObject()) {
            flattenedMap.put(parentKey, jsonNode.asText());
            return flattenedMap;
        }
        for (Map.Entry<String, JsonNode> field : jsonNode.properties()) {
            String key = field.getKey();
            JsonNode value = field.getValue();
            String newKey = parentKey.isEmpty() ? key : parentKey + "." + key;
            if (value.isObject()) {
                flattenedMap.putAll(flattenJson(value, newKey));
            } else if (value.isArray()) {
                for (int i = 0; i < value.size(); i++) {
                    flattenedMap.put(newKey + "[" + i + "]", value.get(i));
                }
            } else {
                flattenedMap.put(newKey, value.asText());
            }
        }
        return flattenedMap;
    }

    @SneakyThrows({JsonProcessingException.class})
    public static JsonNode deepMerge(String ...jsons) {
        ObjectMapper objectMapper1 = getObjectMapper();
        if (jsons.length == 0) {
            return objectMapper1.createObjectNode();
        }
        if (jsons.length == 1) {
            return objectMapper1.readTree(jsons[0]);
        } else {
            JsonNode result = objectMapper1.readTree(jsons[0]);
            for (int i = 1; i < jsons.length; i++) {
                deepMerge(result, objectMapper1.readTree(jsons[i]));
            }
            return result;
        }
    }

    public static ObjectMapper getObjectMapper() {
       return objectMapper != null ? objectMapper : DEFAULT_OBJECT_MAPPER;
    }

    private static void deepMerge(JsonNode target, JsonNode source) {
        for (Map.Entry<String, JsonNode> field : source.properties()) {
            String fieldName = field.getKey();
            JsonNode jsonNode = field.getValue();

            if (jsonNode.isObject() && target.has(fieldName) && target.get(fieldName).isObject()) {
                // 如果是对象并且目标也有这个字段，则递归合并
                deepMerge(target.get(fieldName), jsonNode);
            } else {
                // 否则，覆盖或者添加新的字段
                ((ObjectNode) target).set(fieldName, jsonNode.deepCopy());
            }
        }
    }
}
