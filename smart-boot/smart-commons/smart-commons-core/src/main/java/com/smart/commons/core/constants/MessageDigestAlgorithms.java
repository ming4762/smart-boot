package com.smart.commons.core.constants;

import lombok.Getter;

/**
 * @author ShiZhongMing
 * 2022/5/18
 * @since 2.0.0
 */
public enum MessageDigestAlgorithms {
    /**
     * 加密方式
     */
    MD2("MD2"),
    MD5("MD5"),
    SHA_1("SHA-1"),
    HA_224("HA-224"),
    SHA_256("SHA-256"),
    SHA_384("SHA-384"),
    SHA_512("SHA-512"),
    SHA_512_224("SHA-512/224"),
    SHA_512_256("SHA-512/256"),
    SHA3_224("SHA3-224"),
    SHA3_256("SHA3-256"),
    SHA3_384("SHA3-384"),
    SHA3_512("SHA3-512")
    ;

    @Getter
    private final String algorithms;

    MessageDigestAlgorithms(String algorithms) {
        this.algorithms = algorithms;
    }
}
