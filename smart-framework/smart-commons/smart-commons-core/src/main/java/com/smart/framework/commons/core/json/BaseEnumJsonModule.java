package com.smart.framework.commons.core.json;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 *
 * @author shizhongming
 * 2025/10/16 11:13
 * @since 5.0.0
 */
public class BaseEnumJsonModule extends SimpleModule {

    public BaseEnumJsonModule() {
        super("BaseEnumJsonModule");
        // 注册 BaseEnum 序列化器
        addSerializer((Class) BaseEnum.class, new BaseEnumJsonConverter.Serializer());

        setDeserializerModifier(new BeanDeserializerModifier() {

            @Override
            public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config, JavaType type, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                Class<?> raw = type.getRawClass();
                if (BaseEnum.class.isAssignableFrom(raw)) {
                    return new BaseEnumJsonConverter.Deserializer(raw);
                }
                return deserializer;
            }
        });
    }
}
