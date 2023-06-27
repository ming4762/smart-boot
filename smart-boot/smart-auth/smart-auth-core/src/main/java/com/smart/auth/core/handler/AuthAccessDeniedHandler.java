package com.smart.auth.core.handler;

import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.i18n.I18nUtils;
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

    private static final String COMMON_ERROR_MESSAGE = "Access Denied";
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException {
        String message = e.getMessage();
        if (COMMON_ERROR_MESSAGE.equals(message)) {
            message = I18nUtils.get(HttpStatus.FORBIDDEN);
        }
        RestJsonWriter.writeJson(response, Result.ofStatus(HttpStatus.FORBIDDEN, message));
    }
}
