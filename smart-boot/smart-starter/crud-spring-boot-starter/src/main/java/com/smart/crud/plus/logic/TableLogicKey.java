package com.smart.crud.plus.logic;

import java.lang.annotation.*;

/**
 * 标记逻辑删除的key
 * @author shizhongming
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TableLogicKey {

    /**
     * 逻辑删除key生成策略
     * @return LogicKeyStrategy
     */
    LogicKeyStrategy strategy() default LogicKeyStrategy.ID;
}
