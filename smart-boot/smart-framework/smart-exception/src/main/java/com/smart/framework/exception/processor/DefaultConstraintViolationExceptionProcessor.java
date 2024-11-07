package com.smart.framework.exception.processor;

import com.smart.framework.commons.core.http.HttpStatus;
import com.smart.framework.commons.core.i18n.I18nUtils;
import com.smart.framework.commons.core.message.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.lang.Nullable;

/**
 * @author ShiZhongMing
 * 2021/3/5 10:31
 * @since 1.0
 */
public class DefaultConstraintViolationExceptionProcessor extends AbstractTypeExceptionMessageProcessor<ConstraintViolationException> {
    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object message(ConstraintViolationException e, long exceptionNo, @Nullable HttpServletRequest request) {
        return Result.failure(HttpStatus.BAD_REQUEST.getCode(), I18nUtils.get(HttpStatus.BAD_REQUEST));
    }
}
