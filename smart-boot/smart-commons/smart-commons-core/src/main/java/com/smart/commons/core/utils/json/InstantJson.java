package com.smart.commons.core.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;

/**
 * Instant 类型json处理
 * @author ShiZhongMing
 * 2021/3/26 10:16
 * @since 1.0
 */
public class InstantJson {

    private InstantJson() {
        // nothing
    }

    /**
     * 反序列化
     */
    public static class InstantDeserializer extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String text = jsonParser.getText();
            if (text == null) {
                return null;
            }
            long aLong = Long.parseLong(text);
            return Instant.ofEpochMilli(aLong);
        }
    }

    /**
     * 序列化
     */
    public static class InstantSerializer extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (instant != null) {
                jsonGenerator.writeNumber(instant.toEpochMilli());
            }
        }
    }
}
