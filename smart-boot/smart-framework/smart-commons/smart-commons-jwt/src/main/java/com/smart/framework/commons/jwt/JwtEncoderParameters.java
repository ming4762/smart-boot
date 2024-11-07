package com.smart.framework.commons.jwt;

import com.smart.framework.commons.jwt.claim.JwtClaimsSet;
import com.smart.framework.commons.jwt.header.JwsHeader;
import lombok.Builder;
import lombok.Getter;

/**
 * @author ShiZhongMing
 * 2022/8/8 16:26
 * @since 1.0
 */
@Builder
@Getter
public class JwtEncoderParameters {

    private final JwsHeader jwsHeader;

    private final JwtClaimsSet claims;
}
