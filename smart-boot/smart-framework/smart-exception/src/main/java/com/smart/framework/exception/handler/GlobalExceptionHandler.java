package com.smart.framework.exception.handler;

import com.smart.framework.commons.core.utils.SmartIdGenerator;
import com.smart.framework.exception.notice.AsyncNoticeHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * 全局异常管理
 * @author shizhongming
 * 2020/2/15 7:12 下午
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    private final AsyncNoticeHandler asyncNoticeHandler;

    private final ExceptionMessageHandler exceptionMessageHandler;

    public GlobalExceptionHandler(AsyncNoticeHandler asyncNoticeHandler, ExceptionMessageHandler exceptionMessageHandler) {
        this.asyncNoticeHandler = asyncNoticeHandler;
        this.exceptionMessageHandler = exceptionMessageHandler;
    }


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object handlerException(Exception e, HttpServletRequest request) {
        if (e instanceof UndeclaredThrowableException undeclaredThrowableException) {
            Throwable throwable = undeclaredThrowableException.getUndeclaredThrowable();
            if (throwable instanceof Exception exception) {
                e = exception;
            }
        }
        // 异常信息生成一个no
        long exceptionNo = SmartIdGenerator.nextId();
        // 处理异常通知
        this.asyncNoticeHandler.noticeException(e, exceptionNo, request);
        // 返回异常处理信息
        return this.exceptionMessageHandler.message(e, exceptionNo, request);
    }
}
