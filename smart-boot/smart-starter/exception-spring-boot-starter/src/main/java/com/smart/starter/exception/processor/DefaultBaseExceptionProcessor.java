package com.smart.starter.exception.processor;

import com.smart.commons.core.exception.BaseException;
import com.smart.commons.core.message.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ShiZhongMing
 * 2021/3/4 14:56
 * @since 1.0
 */
@Slf4j
public class DefaultBaseExceptionProcessor extends AbstractTypeExceptionMessageProcessor<BaseException> {
    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object message(BaseException e, long exceptionNo, @Nullable HttpServletRequest request) {
        log.error(String.format("DataManagerException: 状态码 %s, 异常信息 %s", e.getCode(), e.getMessage()), e.getE());
        return Result.failure(e);
    }
}
