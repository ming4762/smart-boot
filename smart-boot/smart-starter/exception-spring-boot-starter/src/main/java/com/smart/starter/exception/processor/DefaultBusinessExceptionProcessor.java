package com.smart.starter.exception.processor;

import com.smart.commons.core.exception.BusinessException;
import com.smart.commons.core.message.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ShiZhongMing
 * 2021/3/4 15:16
 * @since 1.0
 */
@Slf4j
public class DefaultBusinessExceptionProcessor extends AbstractTypeExceptionMessageProcessor<BusinessException> {
    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object message(BusinessException e, long exceptionNo, @Nullable HttpServletRequest request) {
        log.error("系统发生业务异常", e);
        return Result.failure(e);
    }
}
