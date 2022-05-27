package com.smart.auth.core.handler;

import com.smart.auth.core.exception.AuthException;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.RestJsonWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 未登录入口
 * @author shizhongming
 * 2020/1/16 9:22 下午
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Result<String> result = Result.failure(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        if (e instanceof AuthException) {
            result = Result.failure(((AuthException) e).getCode(), e.getMessage());
        }
        RestJsonWriter.writeJson(httpServletResponse, result);
    }
}
