package com.smart.auth.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

/**
 * token工具类
 * @author zhongming4762
 * 2023/3/10
 */
public class TokenUtils {

    public static final String HEADER_CAPTCHA_TOKEN = "Captcha-Token";

    private static final String UPGRADE_WEBSOCKET = "websocket";

    private TokenUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取jwt
     * @param request 请求体
     * @return JWT
     */
    @Nullable
    public static String getToken(HttpServletRequest request) {
        if (UPGRADE_WEBSOCKET.equals(request.getHeader(HttpHeaders.UPGRADE))) {
            return request.getHeader(com.google.common.net.HttpHeaders.SEC_WEBSOCKET_PROTOCOL);
        }
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    /**
     * 获取行为验证码token
     * @param request 请求体
     * @return token
     */
    @Nullable
    public static String getCaptchaToken(HttpServletRequest request) {
        return request.getHeader(HEADER_CAPTCHA_TOKEN);
    }
}
