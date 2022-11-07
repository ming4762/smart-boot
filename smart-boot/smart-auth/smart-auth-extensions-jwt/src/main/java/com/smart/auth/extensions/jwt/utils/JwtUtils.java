package com.smart.auth.extensions.jwt.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * jwt工具类
 * @author jackson
 * 2020/2/14 10:34 下午
 */
@Slf4j
public class JwtUtils {

    private JwtUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取jwt
     * @param request 请求体
     * @return JWT
     */
    @Nullable
    public static String getJwt(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
