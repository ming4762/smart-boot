package com.smart.starter.exception.handler;

import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.utils.IdGenerator;
import com.smart.starter.exception.notice.AsyncNoticeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
        // 异常信息生成一个no
        long exceptionNo = IdGenerator.nextId();
        // 处理异常通知
        this.asyncNoticeHandler.noticeException(e, exceptionNo, AuthUtils.getCurrentUser(), request);
        // 返回异常处理信息
        return this.exceptionMessageHandler.message(e, exceptionNo, request);
    }
}
