package com.smart.auth.extensions.saml2.handler;

import com.smart.auth.core.handler.AuthLoginSuccessHandler;
import com.smart.auth.extensions.saml2.utils.SamlRetryTimerHolder;
import com.smart.commons.core.utils.IpUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;

/**
 * @author ShiZhongMing
 * 2021/3/1 10:03
 * @since 1.0
 */
public class SamlRetryAuthLoginSuccessHandler extends AuthLoginSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // 登录成功后重置重试
        SamlRetryTimerHolder.reset(IpUtils.getIpAddr(httpServletRequest));
        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
    }
}
