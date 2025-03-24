package com.smart.framework.exception.processor;

import com.smart.framework.commons.core.exception.BusinessException;
import com.smart.framework.commons.core.message.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

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
