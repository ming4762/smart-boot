package com.smart.framework.commons.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.util.TreeMap;

/**
 * json签名工具类
 * @author shizhongming
 * 2025/9/9 17:27
 * @since 5.0.0
 */
public class JsonSignerUtils {

    private JsonSignerUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 返回规范化排序后的 JSON 字符串
     */
    @SneakyThrows(Exception.class)
    public static String canonicalJsonString(String json) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        JsonNode node = OBJECT_MAPPER.readTree(json);
        JsonNode sortedNode = sortJson(node);
        // 不要换行、不要缩进
        OBJECT_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, false);
        return OBJECT_MAPPER.writeValueAsString(sortedNode);
    }

    /**
     * 将 JSON 对象规范化排序，递归处理嵌套对象
     */
    public static JsonNode sortJson(JsonNode node) {
        if (node.isObject()) {
            ObjectNode sortedNode = JsonNodeFactory.instance.objectNode();
            TreeMap<String, JsonNode> sortedMap = new TreeMap<>();
            node.properties().forEach(entry -> sortedMap.put(entry.getKey(), entry.getValue()));
            for (String key : sortedMap.keySet()) {
                sortedNode.set(key, sortJson(sortedMap.get(key)));
            }
            return sortedNode;
        } else if (node.isArray()) {
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            for (JsonNode item : node) {
                arrayNode.add(sortJson(item));
            }
            return arrayNode;
        } else {
            // 基本类型直接返回
            return node;
        }
    }
}
