package com.smart.starter.exception.processor;

import com.smart.commons.core.message.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ShiZhongMing
 * 2021/9/26 13:41
 * @since 1.0
 */
@Slf4j
public class DefaultAsyncRequestTimeoutExceptionProcessor extends AbstractTypeExceptionMessageProcessor<AsyncRequestTimeoutException> {
    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object message(AsyncRequestTimeoutException e, long exceptionNo, @Nullable HttpServletRequest request) {
        log.error("请求超时");
        return Result.failure("请求超时");
    }
}
