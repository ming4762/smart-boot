package com.smart.framework.auth.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * token工具类
 * @author zhongming4762
 * 2023/3/10
 */
public class TokenUtils {

    public static final String HEADER_CAPTCHA_TOKEN = "Captcha-Token";

    private static final String UPGRADE_WEBSOCKET = "websocket";

    private static final String BEARER_PREFIX = "Bearer ";

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
        String jwt;
        if (UPGRADE_WEBSOCKET.equals(request.getHeader(HttpHeaders.UPGRADE))) {
            jwt = request.getHeader(com.google.common.net.HttpHeaders.SEC_WEBSOCKET_PROTOCOL);
        } else {
            jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        }
        if (!StringUtils.hasText(jwt)) {
            return null;
        }
        if (jwt.startsWith(BEARER_PREFIX)) {
            jwt = jwt.substring(7);
        }
        return jwt;
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
