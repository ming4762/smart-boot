package com.smart.auth.core.handler;

import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.RestJsonWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * 登出成功执行器
 * @author shizhongming
 * 2020/1/17 7:41 下午
 */
public class AuthLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        RestJsonWriter.writeJson(httpServletResponse, Result.success(null, "登出成功"));
    }
}
