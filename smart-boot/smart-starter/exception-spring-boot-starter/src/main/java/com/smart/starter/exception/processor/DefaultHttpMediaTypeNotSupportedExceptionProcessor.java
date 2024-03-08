package com.smart.starter.exception.processor;

import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.message.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpMediaTypeNotSupportedException;

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
    public Object message(HttpMediaTypeNotSupportedException e, long exceptionNo, @Nullable HttpServletRequest request) {
        log.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getMessage(), e);
        final String message = this.i18nMessage(HttpStatus.UNSUPPORTED_MEDIA_TYPE, HttpStatus.UNSUPPORTED_MEDIA_TYPE.getMessage());
        return Result.ofStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE, message);
    }
}
