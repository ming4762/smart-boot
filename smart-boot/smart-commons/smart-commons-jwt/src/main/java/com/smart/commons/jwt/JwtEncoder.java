package com.smart.commons.jwt;

import org.springframework.lang.NonNull;

/**
 * @author ShiZhongMing
 * 2022/8/8
 * @since 3.0.0
 */
@FunctionalInterface
public interface JwtEncoder {

    /**
     * jwt编码
     * @param parameter 编码参数
     * @return jwt
     */
    Jwt encode(@NonNull JwtEncoderParameters parameter);
}
