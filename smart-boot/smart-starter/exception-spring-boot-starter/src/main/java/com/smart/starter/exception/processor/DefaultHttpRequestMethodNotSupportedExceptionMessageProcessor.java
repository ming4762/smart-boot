package com.smart.starter.exception.processor;

import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * @author ShiZhongMing
 * 2021/3/3 17:37
 * @since 1.0
 */
@Slf4j
public class DefaultHttpRequestMethodNotSupportedExceptionMessageProcessor extends AbstractI18nExceptionMessageProcessor<HttpRequestMethodNotSupportedException> {
    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object message(HttpRequestMethodNotSupportedException e, long exceptionNo, @Nullable HttpServletRequest request) {
        log.error("HttpRequestMethodNotSupportedException: 当前请求方式 {}, 支持请求方式 {}", e.getMethod(), JsonUtils.toJsonString(e.getSupportedHttpMethods()));
        final String message = this.i18nMessage(HttpStatus.METHOD_NOT_ALLOWED, e.toString(), e.getMethod(), JsonUtils.toJsonString(e.getSupportedHttpMethods()));
        return Result.failure(message);
    }
}
