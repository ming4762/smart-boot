package com.smart.framework.commons.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;

/**
 * 基于jackson-dataformat-xml的XML工具类
 * @author shizhongming
 * 2025/6/18 15:05
 * @since 5.0.0
 */
public class XmlUtils {

    private static final XmlMapper XML_MAPPER = new XmlMapper();

    private XmlUtils() {
        throw new IllegalStateException("Utility class");
    }

    static {
        XML_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        XML_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * 将对象转换为xml字符串
     * @param object 对象
     * @return xml字符串
     */
    @SneakyThrows(JsonProcessingException.class)
    public static String toXml(@NonNull Object object) {
        return XML_MAPPER.writeValueAsString(object);
    }

    /**
     * 将XML转为对象
     * @param xml xml字符串
     * @return 对象
     */
    public static Object parse(@NonNull String xml) {
        return parse(xml, Object.class);
    }

    /**
     * 将XML转为对象
     * @param xml xml字符串
     * @param clazz 目标对象类型
     * @return 对象
     */
    @SneakyThrows({JsonProcessingException.class})
    public static <T> T parse(@NonNull String xml, @NonNull Class<T> clazz) {
        return XML_MAPPER.readValue(xml, clazz);
    }

    /**
     * 将XML转为对象
     * @param xml xml字符串
     * @param typeReference 类型引用
     * @return 对象
     */
    @SneakyThrows({JsonProcessingException.class})
    public static <T> T parse(@NonNull String xml, @NonNull TypeReference<T> typeReference) {
        return XML_MAPPER.readValue(xml, typeReference);
    }
}
