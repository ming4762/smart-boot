package com.smart.commons.jwt.algorithm;

/**
 * @author ShiZhongMing
 * 2022/8/8 16:43
 * @since 1.0
 */
public enum SignatureAlgorithm implements JwaAlgorithm {
    /**
     * RS256
     */
    RS256(JwsAlgorithms.RS256),

    RS384(JwsAlgorithms.RS384),

    RS512(JwsAlgorithms.RS512),

    ES256(JwsAlgorithms.ES256),

    ES384(JwsAlgorithms.ES384),

    ES512(JwsAlgorithms.ES512),

    PS256(JwsAlgorithms.PS256),

    PS384(JwsAlgorithms.PS384),

    PS512(JwsAlgorithms.PS512)
    ;

    private final String name;

    SignatureAlgorithm(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
