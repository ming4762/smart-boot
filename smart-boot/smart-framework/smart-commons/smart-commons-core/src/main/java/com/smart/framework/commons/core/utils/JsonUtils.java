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
import java.util.Iterator;
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

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        OBJECT_MAPPER.registerModule(javaTimeModule);
    }

    /**
     * 对象转为json
     * @param object 实体
     * @return json字符串
     */
    @SneakyThrows(JsonProcessingException.class)
    public static String toJsonString(Object object) {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    /**
     * json转为对象
     * @param json json字符串
     * @return 实体
     */
    @SneakyThrows(JsonProcessingException.class)
    public static Object parse(String json) {
        return OBJECT_MAPPER.readValue(json, Object.class);
    }

    /**
     * json转为对象
     * @param json json字符串
     * @return 实体
     */
    @SneakyThrows(JsonProcessingException.class)
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
    @SneakyThrows(JsonProcessingException.class)
    public static <T> List<T> parseCollection(String json, Class<T> clazz) {
        final JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
        return OBJECT_MAPPER.readValue(json, javaType);
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
        return OBJECT_MAPPER.readValue(json, typeReference);
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
        JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
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
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
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
        if (jsons.length == 0) {
            return OBJECT_MAPPER.createObjectNode();
        }
        if (jsons.length == 1) {
            return OBJECT_MAPPER.readTree(jsons[0]);
        } else {
            JsonNode result = OBJECT_MAPPER.readTree(jsons[0]);
            for (int i = 1; i < jsons.length; i++) {
                deepMerge(result, OBJECT_MAPPER.readTree(jsons[i]));
            }
            return result;
        }
    }

    private static void deepMerge(JsonNode target, JsonNode source) {
        for (Iterator<Map.Entry<String, JsonNode>> it = source.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> field = it.next();
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
