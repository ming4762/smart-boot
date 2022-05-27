package com.smart.auth.core.handler;

import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.RestJsonWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 无权限执行器
 * @author shizhongming
 * 2020/1/16 9:18 下午
 */
public class AuthAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException {
        RestJsonWriter.writeJson(response, Result.ofStatus(HttpStatus.FORBIDDEN, e.getMessage()));
    }
}
