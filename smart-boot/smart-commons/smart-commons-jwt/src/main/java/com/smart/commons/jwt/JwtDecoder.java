package com.smart.commons.jwt;

/**
 * @author ShiZhongMing
 * 2022/8/9 9:40
 * @since 1.0
 */
@FunctionalInterface
public interface JwtDecoder {

    /**
     * JWT解码
     * @param token token
     * @return 解码后的JWT
     */
    Jwt decode(String token);
}
