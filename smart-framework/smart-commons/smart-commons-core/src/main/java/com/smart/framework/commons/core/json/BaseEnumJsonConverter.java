package com.smart.framework.commons.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

/**
 * 基础枚举json转换器
 * @author shizhongming
 * 2025/10/16 10:48
 * @since 5.0.0
 */
public class BaseEnumJsonConverter {

    private BaseEnumJsonConverter() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 通用序列化器
     */
    public static class Serializer extends JsonSerializer<BaseEnum<?>> implements ContextualSerializer {

        @Override
        public void serialize(BaseEnum<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(value == null ? null : value.getValue());
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
            return this;
        }
    }

    /**
     * 通用反序列化器
     */
    public static class Deserializer extends JsonDeserializer<BaseEnum<?>> implements ContextualDeserializer {

        private Class<?> targetType;

        public Deserializer() {}

        public Deserializer(Class<?> targetType) {
            this.targetType = targetType;
        }

        @Override
        public BaseEnum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (targetType == null) {
                return null;
            }
            String text = p.getText();
            if (BaseEnum.class.isAssignableFrom(targetType)) {
                for (Object constant : targetType.getEnumConstants()) {
                    BaseEnum<?> e = (BaseEnum<?>) constant;
                    if (e.getValue().toString().equals(text)) {
                        return e;
                    }
                }
            }
            return null;
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
            if (property != null) {
                JavaType type = property.getType();
                Class<?> raw = type.getRawClass();
                if (raw.isEnum() && BaseEnum.class.isAssignableFrom(raw)) {
                    return new Deserializer(raw);
                }
            }
            return this;
        }
    }
}
