package com.smart.starter.exception.processor;

import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.message.Result;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

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
