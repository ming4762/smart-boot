package com.smart.framework.auth.core.handler;

import com.smart.framework.auth.core.model.LoginResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

/**
 * @author ShiZhongMing
 * 2021/3/1 9:56
 * @since 1.0
 */
public interface AuthSuccessDataHandler {

    /**
     * 登录成功数据
     * @param authentication 认证信息
     * @param request 请求体
     * @param response 响应体
     * @return 登录成功数据
     */
    LoginResult successData(Authentication authentication, HttpServletRequest request, HttpServletResponse response);
}
