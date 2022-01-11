package com.smart.auth.core.handler;

import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.RestJsonWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证失败处理类
 * @author jackson
 * 2020/1/23 7:51 下午
 */
@Slf4j
public class AuthLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("登录时发生错误: " + exception.getMessage(), exception);
        RestJsonWriter.writeJson(response, Result.failure(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()));
    }
}
