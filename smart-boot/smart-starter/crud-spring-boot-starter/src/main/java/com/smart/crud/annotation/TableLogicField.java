package com.smart.crud.annotation;

import com.smart.crud.plus.logic.LogicKeyStrategy;

import java.lang.annotation.*;

/**
 * 逻辑删除注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface TableLogicField {

    /**
     * 是否逻辑删除key
     * @return 是否逻辑删除key
     */
    boolean isDeleteKey() default false;

    /**
     * 删除时是否注入
     * @return 默认不注入
     */
    boolean isFill() default false;

    /**
     * 逻辑删除key生成策略
     * @return LogicKeyStrategy
     */
    LogicKeyStrategy strategy() default LogicKeyStrategy.ID;
}
