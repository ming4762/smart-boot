package com.smart.starter.exception.processor;

import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.message.Result;
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
    public Object message(ConstraintViolationException e, @Nullable HttpServletRequest request) {
        return Result.failure(HttpStatus.BAD_REQUEST.getCode(), I18nUtils.get(HttpStatus.BAD_REQUEST));
    }
}
