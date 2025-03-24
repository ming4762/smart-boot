package com.smart.framework.exception.processor;

import com.smart.framework.commons.core.http.HttpStatus;
import com.smart.framework.commons.core.message.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.lang.Nullable;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.stream.Collectors;

/**
 * 接口验证消息错误信息返回
 * @author shizhongming
 * 2024/4/8 15:11
 * @since 3.0.0
 */
public class DefaultHandlerMethodValidationExceptionProcessor extends AbstractTypeExceptionMessageProcessor<HandlerMethodValidationException> {
    /**
     * 返回的信息
     *
     * @param e           异常
     * @param exceptionNo 异常编号
     * @param request     请求信息
     * @return 信息
     */
    @Override
    public Object message(HandlerMethodValidationException e, long exceptionNo, @Nullable HttpServletRequest request) {
        String message = e.getParameterValidationResults().stream()
                .flatMap(item -> item.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return Result.failure(HttpStatus.BAD_REQUEST.getCode(), message);
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
