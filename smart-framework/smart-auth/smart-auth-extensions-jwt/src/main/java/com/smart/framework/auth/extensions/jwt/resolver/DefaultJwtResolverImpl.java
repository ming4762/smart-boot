package com.smart.framework.auth.extensions.jwt.resolver;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.model.RestUserDetailsImpl;
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

    @Override
    public RestUserDetails resolver(@NonNull String jwtStr) {
        try {
            Jwt jwt = this.jwtDecoder.decode(jwtStr);
            Map<String, Object> claims = jwt.getClaims();
            RestUserDetailsImpl userDetails = JsonUtils.parse((String) claims.get(USER_KEY), RestUserDetailsImpl.class);
            userDetails.setToken(jwtStr);
            return userDetails;
        } catch (JwtExpiredException e) {
            log.error("jwt已过期:{}", e.getMessage());
            return null;
        }
    }

    @Override
    public String create(@NonNull RestUserDetails userDetails, @NonNull Duration effective, Object payload) {
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(effective))
                .id(userDetails.getUserId().toString());
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
}
