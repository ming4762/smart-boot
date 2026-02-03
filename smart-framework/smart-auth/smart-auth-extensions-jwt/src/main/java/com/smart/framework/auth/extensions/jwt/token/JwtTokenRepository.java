package com.smart.framework.auth.extensions.jwt.token;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.token.SmartTokenRepository;
import org.springframework.lang.NonNull;

/**
 * jwt token存储器
 * @author shizhongming
 * 2025/3/23 17:25
 * @since 5.0.0
 */
public interface JwtTokenRepository extends SmartTokenRepository {

    /**
     * 通过refreshToken申请token
     * @param refreshToken refreshToken
     * @return token
     */
    String applyToken(String refreshToken);

    /**
     * 生成token
     * @param userDetails 用户信息
     * @return token
     */
    String generateToken(@NonNull RestUserDetails userDetails);

}
