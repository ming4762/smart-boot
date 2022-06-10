package com.smart.starter.exception.processor;

import com.smart.commons.core.message.Result;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ShiZhongMing
 * 2021/3/3 17:45
 * @since 1.0
 */
public class DefaultHttpMessageNotReadableExceptionMessageProcessor extends AbstractI18nExceptionMessageProcessor<HttpMessageNotReadableException> {
    @Override
    public Object message(HttpMessageNotReadableException e, long exceptionNo, @Nullable HttpServletRequest request) {
        return Result.failure("数据读取失败", e.getMessage());
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
