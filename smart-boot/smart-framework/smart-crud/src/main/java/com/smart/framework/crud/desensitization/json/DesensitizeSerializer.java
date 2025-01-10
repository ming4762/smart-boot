package com.smart.framework.crud.desensitization.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.smart.framework.commons.core.utils.ApplicationContextUtils;
import com.smart.framework.crud.desensitization.DesensitizeType;
import com.smart.framework.crud.desensitization.annotation.Desensitize;
import com.smart.framework.crud.desensitization.handler.DesensitizeHandler;
import com.smart.framework.crud.desensitization.handler.NoneDesensitizeHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * 脱敏序列化器
 * @author shizhongming
 * 2025/1/9 20:57
 * @since 5.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
public class DesensitizeSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private Desensitize desensitize;

    @SneakyThrows({NoSuchMethodException.class, InstantiationException.class, IllegalAccessException.class, IllegalArgumentException.class, InvocationTargetException.class})
    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        if (value == null) {
            jsonGenerator.writeNull();
            return;
        }
        DesensitizeType desensitizeType = this.desensitize.type();
        Class<? extends DesensitizeHandler> handlerClass = this.desensitize.handler();
        if (NoneDesensitizeHandler.class.equals(handlerClass)) {
            handlerClass = desensitizeType.getHandler();
        }
        DesensitizeHandler desensitizeHandler = ApplicationContextUtils.getBean(handlerClass);
        if (desensitizeHandler == null) {
            desensitizeHandler = handlerClass.getDeclaredConstructor().newInstance();
        }
        String result = desensitizeHandler.desensitize(value);
        jsonGenerator.writeString(result);
    }


    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty property) throws JsonMappingException {
        if (property == null) {
            return serializerProvider.findNullValueSerializer(null);
        }
        Desensitize desensitizeAnnotation = property.getAnnotation(Desensitize.class);
        if (desensitizeAnnotation == null) {
            return serializerProvider.findValueSerializer(property.getType(), property);
        }
        return new DesensitizeSerializer(desensitizeAnnotation);
    }
}
