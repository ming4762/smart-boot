package com.smart.starter.exception.processor;

import com.smart.commons.core.message.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;

/**
 * @author ShiZhongMing
 * 2021/3/5 10:29
 * @since 1.0
 */
public class DefaultBindExceptionProcessor extends AbstractTypeExceptionMessageProcessor<BindException> {
    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object message(BindException e, long exceptionNo, @Nullable HttpServletRequest request) {
        return Result.failure(e.getBindingResult());
    }
}
