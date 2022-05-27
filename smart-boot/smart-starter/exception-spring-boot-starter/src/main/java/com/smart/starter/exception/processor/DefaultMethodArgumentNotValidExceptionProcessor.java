package com.smart.starter.exception.processor;

import com.smart.commons.core.message.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;

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
    public Object message(MethodArgumentNotValidException e, @Nullable HttpServletRequest request) {
        return Result.failure(e.getBindingResult());
    }
}
