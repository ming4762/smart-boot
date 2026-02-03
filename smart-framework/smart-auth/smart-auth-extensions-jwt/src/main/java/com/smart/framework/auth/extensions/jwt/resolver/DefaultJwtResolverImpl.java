package com.smart.framework.auth.extensions.jwt.resolver;

import com.smart.framework.auth.core.model.RestUserDetailsImpl;
import com.smart.framework.auth.extensions.jwt.data.JwtRefreshTokenPayload;
import com.smart.framework.auth.extensions.jwt.data.JwtTokenResolverUser;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.commons.jwt.Jwt;
import com.smart.framework.commons.jwt.JwtDecoder;
import com.smart.framework.commons.jwt.JwtEncoder;
import com.smart.framework.commons.jwt.JwtEncoderParameters;
import com.smart.framework.commons.jwt.algorithm.SignatureAlgorithm;
import com.smart.framework.commons.jwt.claim.JwtClaimsSet;
import com.smart.framework.commons.jwt.exception.JwtExpiredException;
import com.smart.framework.commons.jwt.header.JwsHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;


/**
 * JWT服务层
 * 1、登出逻辑
 * 2、jwt解析接口
 * 3、jwtStore接口 cache实现
 * @author ShiZhongMing
 * 2020/12/31 15:43
 * @since 1.0
 */
@Slf4j
public class DefaultJwtResolverImpl implements JwtResolver {

    private static final String USER_KEY = "user";

    private JwtDecoder jwtDecoder;
    private JwtEncoder jwtEncoder;

    /**
     * 解析jwt token，获取用户信息
     *
     * @param jwtStr jwt
     * @return 用户信息
     */
    @Override
    public JwtTokenResolverUser resolverToken(@NonNull String jwtStr) {
        JwtTokenResolverData data = this.resolverJwtUserPayload(jwtStr);
        if (data == null) {
            return null;
        }
        RestUserDetailsImpl userDetails = JsonUtils.parse(data.userData, RestUserDetailsImpl.class);
        userDetails.setToken(jwtStr);
        return new JwtTokenResolverUser(userDetails, data.issuedAt, data.expiresAt);
    }

    /**
     * 解析刷新token
     *
     * @param refreshToken 刷新token
     * @return 解析结果
     */
    @Override
    public JwtRefreshTokenPayload resolverRefreshToken(@NonNull String refreshToken) {
        JwtTokenResolverData data = this.resolverJwtUserPayload(refreshToken);
        if (data == null) {
            return null;
        }
        return JsonUtils.parse(data.userData, JwtRefreshTokenPayload.class);
    }

    /**
     * 获取jwt载荷信息-用户信息
     *
     * @param jwtStr jwt字符串
     * @return 用户信息字符串
     */
    private JwtTokenResolverData resolverJwtUserPayload(String jwtStr) {
        try {
            Jwt jwt = this.jwtDecoder.decode(jwtStr);
            Map<String, Object> claims = jwt.getClaims();
            return new JwtTokenResolverData(
                    (String) claims.get(USER_KEY),
                    jwt.getIssuedAt(),
                    jwt.getExpiresAt());
        } catch (JwtExpiredException e) {
            log.error("jwt已过期:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 创建JWT
     *
     * @param userId    用户ID
     * @param effective 有效时间
     * @param payload   载荷信息
     * @return jwt字符串
     */
    @Override
    public String create(@NonNull Long userId, @NonNull Duration effective, Object payload) {
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(effective))
                .id(userId.toString());
        if (payload != null) {
            builder.claim(USER_KEY, JsonUtils.toJsonString(payload));
        }
        JwtEncoderParameters parameters = JwtEncoderParameters.builder()
                .jwsHeader(JwsHeader.builder().algorithm(SignatureAlgorithm.RS256).build())
                .claims(builder.build())
                .build();
        return this.jwtEncoder.encode(parameters).getTokenValue();
    }


    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Autowired
    public void setJwtDecoder(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Autowired
    public void setJwtEncoder(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    private record JwtTokenResolverData(
            String userData,
            Instant issuedAt,
            Instant expiresAt) {
    }
}
