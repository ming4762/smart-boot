package com.smart.starter.exception.processor;

import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.message.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ShiZhongMing
 * 2021/3/4 15:00
 * @since 1.0
 */
@Slf4j
public class DefaultHttpMediaTypeNotSupportedExceptionProcessor extends AbstractI18nExceptionMessageProcessor<HttpMediaTypeNotSupportedException> {
    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object message(HttpMediaTypeNotSupportedException e, @Nullable HttpServletRequest request) {
        log.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getMessage(), e);
        final String message = this.i18nMessage(HttpStatus.PARAM_NOT_MATCH, HttpStatus.PARAM_NOT_MATCH.getMessage());
        return Result.failure(message);
    }
}
