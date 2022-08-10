package com.smart.commons.jwt;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.smart.commons.jwt.algorithm.SignatureAlgorithm;
import com.smart.commons.jwt.claim.JwtClaimsSet;
import com.smart.commons.jwt.header.JwsHeader;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

/**
 * @author ShiZhongMing
 * 2022/8/9 10:39
 * @since 1.0
 */
public class Test {

    private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

    private static final String KEY_ALGORITHM = "RSA";

    public static void main(String[] args) throws Exception {
        test2();
    }

    private static void test2() throws IOException, ClassNotFoundException {
        String pubPath = "E:\\Documents\\workspace\\temp\\app.pub";
    }

    public static void test1() throws NoSuchAlgorithmException {
        // 生成秘钥
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        JwtDecoder decoder = NimbusJwtDecoder.withPublicKey(publicKey).build();

        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        JwtEncoder encoder = new NimbusJwtEncoder(jwkSource);

        Jwt jwt = encoder.encode(JwtEncoderParameters.builder()
                        .jwsHeader(
                                JwsHeader.builder()
                                        .algorithm(SignatureAlgorithm.RS256)
                                        .type("JWT")
                                        .build()
                        )
                        .claims(
                                JwtClaimsSet.builder()
                                        .issuedAt(Instant.now())
                                        .subject("ceshi")
                                        .claim("username", "张三")
                                        .build()
                        )
                .build());
        System.out.println(jwt.getTokenValue());

        Jwt decodeJwt = decoder.decode(jwt.getTokenValue());

        System.out.println(decodeJwt);
    }
}
