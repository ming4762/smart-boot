package com.smart.starter.exception.handler;

import com.smart.auth.core.utils.AuthUtils;
import com.smart.starter.exception.notice.AsyncNoticeHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
        // 处理异常通知
        this.asyncNoticeHandler.noticeException(e, AuthUtils.getCurrentUser(), request);
        // 返回异常处理信息
        return this.exceptionMessageHandler.message(e, request);
    }
}
