package com.smart.crud.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Locale;

/**
 * @author ShiZhongMing
 * 2021/11/17
 * @since 1.0.7
 */
public class LocaleJson {

    private LocaleJson() {
        throw new IllegalStateException("Utility class");
    }

    public static class LocaleDeserializer extends JsonDeserializer<Locale> {
        @Override
        public Locale deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String text = jsonParser.getText();
            if (text == null) {
                return null;
            }
            return Locale.forLanguageTag(text);
        }
    }

    public static class LocaleSerializer extends JsonSerializer<Locale> {
        @Override
        public void serialize(Locale locale, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (locale != null) {
                jsonGenerator.writeString(locale.toLanguageTag());
            }
        }
    }
}
