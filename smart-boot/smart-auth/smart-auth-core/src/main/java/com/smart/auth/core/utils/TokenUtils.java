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
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
