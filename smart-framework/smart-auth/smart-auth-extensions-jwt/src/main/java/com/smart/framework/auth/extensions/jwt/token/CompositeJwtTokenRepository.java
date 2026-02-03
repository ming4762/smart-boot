package com.smart.framework.auth.extensions.jwt.token;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.token.CompositeSmartTokenRepository;
import com.smart.framework.auth.core.token.SmartTokenRepository;
import lombok.NonNull;

import java.util.List;

/**
 * 基于组合模式代理多个JwtTokenRepository
 * @author shizhongming
 * 2025/3/24 16:01
 * @since 5.0.0
 */
public class CompositeJwtTokenRepository extends CompositeSmartTokenRepository implements JwtTokenRepository {

    public CompositeJwtTokenRepository(List<JwtTokenRepository> jwtTokenRepositoryList) {
        super(jwtTokenRepositoryList.stream().map(SmartTokenRepository.class::cast).toList());
    }

    /**
     * 通过refreshToken申请token
     *
     * @param refreshToken refreshToken
     * @return token
     */
    @Override
    public String applyToken(String refreshToken) {
        return this.forGet(repository -> ((JwtTokenRepository)repository).applyToken(refreshToken));
    }

    /**
     * 生成token
     *
     * @param userDetails 用户信息
     * @return token
     */
    @Override
    public String generateToken(@NonNull RestUserDetails userDetails) {
        return this.forGet(repository -> ((JwtTokenRepository)repository).generateToken(userDetails));
    }
}
