package com.smart.framework.auth.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
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

    private static final String REFRESH_TOKEN = HttpHeaders.AUTHORIZATION + "-refreshToken";

    private TokenUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取token
     * @param request 请求体
     * @return token
     */
    @Nullable
    public static String getToken(HttpServletRequest request) {
        String token;
        if (UPGRADE_WEBSOCKET.equals(request.getHeader(HttpHeaders.UPGRADE))) {
            token = request.getHeader(com.google.common.net.HttpHeaders.SEC_WEBSOCKET_PROTOCOL);
        } else {
            token = request.getHeader(HttpHeaders.AUTHORIZATION);
        }
        if (!StringUtils.hasText(token)) {
            return null;
        }
        if (token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
        }
        return token;
    }

    /**
     * 获取刷新token
     * @param request 请求体
     * @return token
     */
    public static String getRefreshToken(HttpServletRequest request) {
        return request.getHeader(REFRESH_TOKEN);
    }

    /**
     * 设置token写入响应头
     * @param token token
     * @param response 响应体
     */
    public static void setHeaderToken(String token, HttpServletResponse response) {
        response.setHeader(HttpHeaders.AUTHORIZATION, token);
    }

    /**
     * 刷新token写入响应头
     * @param refreshToken 刷新token
     * @param response 响应体
     */
    public static void setHeaderRefreshToken(String refreshToken, HttpServletResponse response) {
        response.setHeader(REFRESH_TOKEN, refreshToken);
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
