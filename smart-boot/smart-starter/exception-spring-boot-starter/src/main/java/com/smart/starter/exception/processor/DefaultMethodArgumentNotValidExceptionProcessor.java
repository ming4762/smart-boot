package com.smart.starter.exception.processor;

import com.smart.commons.core.message.Result;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ShiZhongMing
 * 2021/3/5 10:31
 * @since 1.0
 */
public class DefaultMethodArgumentNotValidExceptionProcessor extends AbstractTypeExceptionMessageProcessor<MethodArgumentNotValidException> {
    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object message(MethodArgumentNotValidException e, long exceptionNo, @Nullable HttpServletRequest request) {
        return Result.failure(e.getBindingResult());
    }
}
