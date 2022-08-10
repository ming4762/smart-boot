package com.smart.commons.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.factories.DefaultJWSSignerFactory;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.produce.JWSSignerFactory;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.smart.commons.jwt.algorithm.SignatureAlgorithm;
import com.smart.commons.jwt.claim.JwtClaimsSet;
import com.smart.commons.jwt.exception.JwtEncodingException;
import com.smart.commons.jwt.header.HeaderNames;
import com.smart.commons.jwt.header.JwsHeader;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ShiZhongMing
 * 2022/8/8
 * @since 3.0.0
 */
public class NimbusJwtEncoder implements JwtEncoder {

    private static final String ENCODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to encode the Jwt: %s";

    private static final JwsHeader DEFAULT_JWS_HEADER = JwsHeader.builder()
            .algorithm(SignatureAlgorithm.RS256)
            .build();

    private final JWKSource<SecurityContext> jwkSource;

    private static final JWSSignerFactory JWS_SIGNER_FACTORY = new DefaultJWSSignerFactory();

    private final Map<JWK, JWSSigner> jwsSigners = new ConcurrentHashMap<>();

    public NimbusJwtEncoder(JWKSource<SecurityContext> jwkSource) {
        Assert.notNull(jwkSource, "jwkSource cannot be null");
        this.jwkSource = jwkSource;
    }

    @Override
    public Jwt encode(@NonNull JwtEncoderParameters parameters) {
        Assert.notNull(parameters, "parameters cannot be null");
        JwsHeader header = parameters.getJwsHeader();
        if (header == null) {
            header = DEFAULT_JWS_HEADER;
        }
        JWK jwk = this.selectJwk(header);
        header = this.addKeyIdentifierHeadersIfNecessary(header, jwk);
        JwtClaimsSet claims = parameters.getClaims();

        String jws = this.serialize(header, claims, jwk);
        return new Jwt(jws, claims.getIssuedAt(), claims.getExpiresAt(), header.getHeaders(), claims.getClaims());
    }

    protected JWSSigner createSigner(JWK jwk) {
        try {
            return JWS_SIGNER_FACTORY.createJWSSigner(jwk);
        } catch (JOSEException ex) {
            throw new JwtEncodingException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE,
                    "Failed to create a JWS Signer -> " + ex.getMessage()), ex);
        }
    }

    protected String serialize(JwsHeader headers, JwtClaimsSet claims, JWK jwk) {
        JWSHeader jwsHeader = this.convert(headers);
        JWTClaimsSet jwtClaimsSet = this.convert(claims);

        JWSSigner jwsSigner = this.jwsSigners.computeIfAbsent(jwk, this::createSigner);
        SignedJWT signedJwt = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            signedJwt.sign(jwsSigner);
        } catch (JOSEException ex) {
            throw new JwtEncodingException(
                    String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Failed to sign the JWT -> " + ex.getMessage()), ex);
        }
        return signedJwt.serialize();
    }

    protected JWSHeader convert(JwsHeader headers) {
        JWSHeader.Builder builder = new JWSHeader.Builder(JWSAlgorithm.parse(headers.getAlgorithm().getName()));

        Map<String, Object> jwk = headers.getJwk();
        if (!CollectionUtils.isEmpty(jwk)) {
            try {
                builder.jwk(JWK.parse(jwk));
            }
            catch (Exception ex) {
                throw new JwtEncodingException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE,
                        "Unable to convert '" + HeaderNames.JWK + "' JOSE header"), ex);
            }
        }

        String keyId = headers.getKeyId();
        if (StringUtils.hasText(keyId)) {
            builder.keyID(keyId);
        }

        String x509SHA256Thumbprint = headers.getX509SHA256Thumbprint();
        if (StringUtils.hasText(x509SHA256Thumbprint)) {
            builder.x509CertSHA256Thumbprint(new Base64URL(x509SHA256Thumbprint));
        }

        String type = headers.getType();
        if (StringUtils.hasText(type)) {
            builder.type(new JOSEObjectType(type));
        }

        Map<String, Object> customHeaders = new HashMap<>();
        headers.getHeaders().forEach((name, value) -> {
            if (!JWSHeader.getRegisteredParameterNames().contains(name)) {
                customHeaders.put(name, value);
            }
        });
        if (!customHeaders.isEmpty()) {
            builder.customParams(customHeaders);
        }

        return builder.build();
    }

    protected JWTClaimsSet convert(JwtClaimsSet claims) {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

        String subject = claims.getSubject();
        if (StringUtils.hasText(subject)) {
            builder.subject(subject);
        }

        Instant expiresAt = claims.getExpiresAt();
        if (expiresAt != null) {
            builder.expirationTime(Date.from(expiresAt));
        }

        Instant issuedAt = claims.getIssuedAt();
        if (issuedAt != null) {
            builder.issueTime(Date.from(issuedAt));
        }

        String jwtId = claims.getId();
        if (StringUtils.hasText(jwtId)) {
            builder.jwtID(jwtId);
        }

        Map<String, Object> customClaims = new HashMap<>();
        claims.getClaims().forEach((name, value) -> {
            if (!JWTClaimsSet.getRegisteredNames().contains(name)) {
                customClaims.put(name, value);
            }
        });
        if (!customClaims.isEmpty()) {
            customClaims.forEach(builder::claim);
        }

        return builder.build();
    }


    protected JwsHeader addKeyIdentifierHeadersIfNecessary(JwsHeader headers, JWK jwk) {
        if (StringUtils.hasText(headers.getKeyId()) && StringUtils.hasText(headers.getX509SHA256Thumbprint())) {
            return headers;
        }

        if (!StringUtils.hasText(jwk.getKeyID()) && jwk.getX509CertSHA256Thumbprint() == null) {
            return headers;
        }
        JwsHeader.Builder builder = JwsHeader.from(headers);
        if (!StringUtils.hasText(headers.getKeyId()) && StringUtils.hasText(jwk.getKeyID())) {
            builder.keyId(jwk.getKeyID());
        }
        if (!StringUtils.hasText(headers.getX509SHA256Thumbprint()) && jwk.getX509CertSHA256Thumbprint() != null) {
            builder.x509SHA256Thumbprint(jwk.getX509CertSHA256Thumbprint().toString());
        }
        return builder.build();
    }

    protected JWK selectJwk(JwsHeader jwsHeader) {
        List<JWK> jwkList;
        try {
            JWKSelector jwkSelector = new JWKSelector(this.createJwkMatcher(jwsHeader));
            jwkList = this.jwkSource.get(jwkSelector, null);
        } catch (Exception e) {
            throw new JwtEncodingException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Failed to select a JWK signing key -> " + e.getMessage()), e);
        }
        if (jwkList.size() > 1) {
            throw new JwtEncodingException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE,
                    "Found multiple JWK signing keys for algorithm '" + jwsHeader.getAlgorithm().getName() + "'"));
        }
        if (jwkList.isEmpty()) {
            throw new JwtEncodingException(
                    String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Failed to select a JWK signing key"));
        }
        return jwkList.get(0);
    }

    protected JWKMatcher createJwkMatcher(JwsHeader jwsHeader) {
        JWSAlgorithm jwsAlgorithm = JWSAlgorithm.parse(jwsHeader.getAlgorithm().getName());
        if (JWSAlgorithm.Family.RSA.contains(jwsAlgorithm) || JWSAlgorithm.Family.EC.contains(jwsAlgorithm)) {
            return new JWKMatcher.Builder()
                    .keyType(KeyType.forAlgorithm(jwsAlgorithm))
                    .keyID(jwsHeader.getKeyId())
                    .keyUses(KeyUse.SIGNATURE, null)
                    .algorithms(jwsAlgorithm, null)
                    .x509CertSHA256Thumbprint(Base64URL.from(jwsHeader.getX509SHA256Thumbprint()))
                    .build();
        } else if (JWSAlgorithm.Family.HMAC_SHA.contains(jwsAlgorithm)) {
            return new JWKMatcher.Builder()
                    .keyType(KeyType.forAlgorithm(jwsAlgorithm))
                    .keyID(jwsHeader.getKeyId())
                    .privateOnly(true)
                    .algorithms(jwsAlgorithm, null)
                    .build();
        }
        return null;
    }
}
