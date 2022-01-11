package com.smart.starter.exception.processor;

import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author ShiZhongMing
 * 2021/3/3 14:25
 * @since 1.0
 */
public abstract class AbstractTypeExceptionMessageProcessor<T extends Exception> implements ExceptionMessageProcessor<T>, InitializingBean {


    private Type type;

    @Override
    public Type processorType() {
        return this.type;
    }

    @Override
    public void afterPropertiesSet() {
        this.type = ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
