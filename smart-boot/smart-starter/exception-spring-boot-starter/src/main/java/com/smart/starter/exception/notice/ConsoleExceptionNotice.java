package com.smart.starter.exception.notice;

import com.smart.auth.core.userdetails.RestUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * 控制台异常通知
 * @author shizhongming
 * 2020/11/15 12:31 上午
 */
@Slf4j
public class ConsoleExceptionNotice extends AbstractCommonExcludeExceptionNotice {

    @Override
    protected void doNotice(@NonNull Exception e, RestUserDetails user, @NonNull HttpServletRequest request) {
        log.info("系统发生异常\n异常信息：{}:{}\n当前用户：{}", e.getClass().getSimpleName(), e.getMessage(), Optional.ofNullable(user).map(RestUserDetails::getFullName).orElse("未登录"));
        log.error("错误信息", e);
    }
}
