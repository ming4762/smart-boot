package com.smart.starter.exception.processor;

import com.smart.commons.core.message.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

/**
 * @author ShiZhongMing
 * 2021/3/3 16:24
 * @since 1.0
 */
@Slf4j
public class DefaultExceptionHandler extends AbstractI18nExceptionMessageProcessor<Exception> {
    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object message(Exception e, @Nullable HttpServletRequest request) {
        log.error("系统发生未知异常", e);
        return Result.failure("系统发生未知异常", e.toString());
    }
}
