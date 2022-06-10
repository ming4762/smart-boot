package com.smart.starter.exception.processor;

import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.message.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * NoHandlerFoundException异常信息处理
 * @author ShiZhongMing
 * 2021/3/3 12:39
 * @since 1.0
 */
@Slf4j
public class DefaultNoHandlerFoundExceptionMessageProcessor extends AbstractI18nExceptionMessageProcessor<NoHandlerFoundException> {

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }


    @Override
    public Object message(NoHandlerFoundException e, long exceptionNo, @Nullable HttpServletRequest request) {
        log.error("NoHandlerFoundException: 请求方法 {}, 请求路径 {}", e.getRequestURL(), e.getHttpMethod(), e);
        final String message = this.i18nMessage(HttpStatus.NOT_FOUND, e.toString(), e.getRequestURL(), e.getHttpMethod());
        return Result.failure(message);
    }

}
