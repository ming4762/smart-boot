package com.smart.framework.ai.dify.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.smart.framework.ai.dify.constants.ChatResponseEventTypeEnum;
import com.smart.framework.ai.dify.constants.EnumValue;
import com.smart.framework.ai.dify.constants.FileTransferMethodEnum;

import java.io.IOException;

/**
 * EnumValue 序列化工具
 * @author shizhongming
 * 2025/2/8 21:01
 * @since 5.0.0
 */
public class EnumValueJson {

    private EnumValueJson() {
        throw new IllegalStateException("Utility class");
    }

    public static class EnumValueSerializer extends JsonSerializer<EnumValue> {

        @Override
        public void serialize(EnumValue value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                gen.writeString(value.getValue());
            }
        }
    }

    public static class FileTransferMethodEnumEnumValueDeserializer extends JsonDeserializer<FileTransferMethodEnum> {
        @Override
        public FileTransferMethodEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String valueAsString = p.getValueAsString();
            if (valueAsString != null) {
                return FileTransferMethodEnum.getByValue(valueAsString);
            }
            return null;
        }
    }

    public static class ChatResponseEventTypeEnumDeserializer extends JsonDeserializer<ChatResponseEventTypeEnum> {
        @Override
        public ChatResponseEventTypeEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String valueAsString = p.getValueAsString();
            if (valueAsString != null) {
                return ChatResponseEventTypeEnum.getByValue(valueAsString);
            }
            return null;
        }
    }
}
