package com.smart.framework.crud.desensitization.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.framework.crud.desensitization.DesensitizeType;
import com.smart.framework.crud.desensitization.handler.DesensitizeHandler;
import com.smart.framework.crud.desensitization.handler.NoneDesensitizeHandler;
import com.smart.framework.crud.desensitization.json.DesensitizeSerializer;

import java.lang.annotation.*;

/**
 * 脱敏注解
 * @author zhongming4762
 * 2023/2/4 20:53
 * @since 5.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizeSerializer.class)
public @interface Desensitize {

    /**
     * 脱敏类型
     * @return 脱敏类型
     */
    DesensitizeType type() default DesensitizeType.ENCODE;

    /**
     * 脱敏处理器
     * 可以手动指定托名处理器
     * @return 脱敏处理器
     */
    Class<? extends DesensitizeHandler> handler() default NoneDesensitizeHandler.class;
}
