package com.smart.starter.exception.processor;

import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;

/**
 * 无权限异常处理器
 * 直接抛出交给spring security处理
 * 如果无此处理会被通用处理器处理，导致spring security无法拦截到异常
 * @author ShiZhongMing
 * 2021/3/4 15:22
 * @since 1.0
 */
public class DefaultAccessDeniedExceptionProcessor extends AbstractTypeExceptionMessageProcessor<AccessDeniedException> {
    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object message(AccessDeniedException e, @Nullable HttpServletRequest request) {
        throw e;
    }
}
