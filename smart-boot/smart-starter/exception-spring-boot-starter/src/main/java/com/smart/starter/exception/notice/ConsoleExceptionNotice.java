package com.smart.starter.exception.notice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

/**
 * 控制台异常通知
 * @author shizhongming
 * 2020/11/15 12:31 上午
 */
@Slf4j
public class ConsoleExceptionNotice extends AbstractCommonExcludeExceptionNotice {

    @Override
    protected void doNotice(@NonNull Exception e, long exceptionNo, @NonNull HttpServletRequest request) {
        log.info("系统发生异常\n异常信息：{}:{}\n异常编号：{}", e.getClass().getSimpleName(), e.getMessage(), exceptionNo);
        log.error("错误信息", e);
    }
}
