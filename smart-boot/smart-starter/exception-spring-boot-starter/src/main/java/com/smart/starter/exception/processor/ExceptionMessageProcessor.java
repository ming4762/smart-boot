package com.smart.starter.exception.processor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;

import java.lang.reflect.Type;

/**
 * @author ShiZhongMing
 * 2021/3/3 12:17
 * @since 1.0
 */
public interface ExceptionMessageProcessor<T extends Exception> {

    /**
     * 处理的异常类型
     * @return 异常类型
     */
    Type processorType();

    /**
     * 处理器序号
     * 同类异常只能有一个处理器，order越小则优先级越高
     * @return 序号
     */
    default int order() {
        return 0;
    }

    /**
     * 返回的信息
     * @param e 异常
     * @param request 请求信息
     * @return 信息
     */
    Object message(T e, @Nullable HttpServletRequest request);
}
