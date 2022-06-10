package com.smart.starter.exception.processor;

import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.message.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ShiZhongMing
 * 2021/3/3 17:43
 * @since 1.0
 */
@Slf4j
public class DefaultMethodArgumentTypeMismatchExceptionMessageProcessor extends AbstractI18nExceptionMessageProcessor<MethodArgumentTypeMismatchException> {
    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object message(MethodArgumentTypeMismatchException e, long exceptionNo, @Nullable HttpServletRequest request) {

        log.error("MethodArgumentTypeMismatchException: 参数名 {}, 异常信息 {}", e.getName(), e.getMessage());
        final String message = this.i18nMessage(HttpStatus.PARAM_NOT_MATCH, e.getName(), e.getMessage());
        return Result.failure(message);
    }
}
