package com.smart.auth.core.handler;

import com.smart.auth.core.constants.LoginTypeEnum;
import com.smart.auth.core.model.LoginResult;
import jakarta.servlet.http.HttpServletRequest;
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
     * @param loginType 登录方式
     * @return 登录成功数据
     */
    LoginResult successData(Authentication authentication, HttpServletRequest request, LoginTypeEnum loginType);
}
