package com.smart.commons.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.proc.SingleKeyJWSKeySelector;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTProcessor;
import com.smart.commons.jwt.algorithm.SignatureAlgorithm;
import com.smart.commons.jwt.converter.MappedJwtClaimSetConverter;
import com.smart.commons.jwt.exception.BadJwtException;
import com.smart.commons.jwt.exception.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * JWT解码
 * @author ShiZhongMing
 * 2022/8/9
 * @since 3.0.0
 */
@Slf4j
public class NimbusJwtDecoder implements JwtDecoder {

    private static final String DECODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to decode the Jwt: %s";

    private final Converter<Map<String, Object>, Map<String, Object>> claimSetConverter = MappedJwtClaimSetConverter
            .withDefaults(Collections.emptyMap());

    private final JWTProcessor<SecurityContext> jwtProcessor;

    public NimbusJwtDecoder(JWTProcessor<SecurityContext> jwtProcessor) {
        Assert.notNull(jwtProcessor, "jwtProcessor cannot be null");
        this.jwtProcessor = jwtProcessor;
    }

    public static PublicKeyJwtDecoderBuilder withPublicKey(RSAPublicKey key) {
        return new PublicKeyJwtDecoderBuilder(key);
    }

    @Override
    public Jwt decode(String token) {
        JWT jwt = this.parse(token);
        if (jwt instanceof PlainJWT) {
            log.trace("Failed to decode unsigned token");
            throw new BadJwtException("Unsupported algorithm of " + jwt.getHeader().getAlgorithm());
        }
        Jwt createJwt = this.createJwt(token, jwt);
        // todo:验证JWT
        return createJwt;
    }

    protected JWT parse(String token) {
        try {
            return JWTParser.parse(token);
        }
        catch (Exception ex) {
            log.trace("Failed to parse token", ex);
            throw new BadJwtException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, ex.getMessage()), ex);
        }
    }

    protected Jwt createJwt(String token, JWT parsedJwt) {
        try {
            JWTClaimsSet jwtClaimsSet = this.jwtProcessor.process(parsedJwt, null);
            Map<String, Object> headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
            Map<String, Object> claims = this.claimSetConverter.convert(jwtClaimsSet.getClaims());
            Assert.notNull(claims, "转换失败，claims为null");
            return Jwt.builder(token)
                    .headers(h -> h.putAll(headers))
                    .claims(c -> c.putAll(claims))
                    .build();
        } catch (JOSEException ex) {
            log.trace("Failed to process JWT", ex);
            throw new JwtException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, ex.getMessage()), ex);
        } catch (Exception ex) {
            log.trace("Failed to process JWT", ex);
            if (ex.getCause() instanceof ParseException) {
                throw new BadJwtException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, "Malformed payload"), ex);
            }
            throw new BadJwtException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, ex.getMessage()), ex);
        }
    }

    public static final class PublicKeyJwtDecoderBuilder {
        private JWSAlgorithm jwsAlgorithm;

        private final RSAPublicKey key;

        private Consumer<ConfigurableJWTProcessor<SecurityContext>> jwtProcessorCustomizer;

        private PublicKeyJwtDecoderBuilder(RSAPublicKey key) {
            Assert.notNull(key, "key cannot be null");
            this.jwsAlgorithm = JWSAlgorithm.RS256;
            this.key = key;
            this.jwtProcessorCustomizer = (processor) -> {
            };
        }

        public PublicKeyJwtDecoderBuilder signatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
            Assert.notNull(signatureAlgorithm, "signatureAlgorithm cannot be null");
            this.jwsAlgorithm = JWSAlgorithm.parse(signatureAlgorithm.getName());
            return this;
        }

        public PublicKeyJwtDecoderBuilder jwtProcessorCustomizer(
                Consumer<ConfigurableJWTProcessor<SecurityContext>> jwtProcessorCustomizer) {
            Assert.notNull(jwtProcessorCustomizer, "jwtProcessorCustomizer cannot be null");
            this.jwtProcessorCustomizer = jwtProcessorCustomizer;
            return this;
        }
        JWTProcessor<SecurityContext> processor() {
            Assert.state(JWSAlgorithm.Family.RSA.contains(this.jwsAlgorithm),
                    () -> "The provided key is of type RSA; however the signature algorithm is of some other type: "
                            + this.jwsAlgorithm + ". Please indicate one of RS256, RS384, or RS512.");
            JWSKeySelector<SecurityContext> jwsKeySelector = new SingleKeyJWSKeySelector<>(this.jwsAlgorithm, this.key);
            DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
            jwtProcessor.setJWSKeySelector(jwsKeySelector);
            // Spring Security validates the claim set independent from Nimbus
            jwtProcessor.setJWTClaimsSetVerifier((claims, context) -> {
            });
            this.jwtProcessorCustomizer.accept(jwtProcessor);
            return jwtProcessor;
        }

        public NimbusJwtDecoder build() {
            return new NimbusJwtDecoder(processor());
        }
    }
}
