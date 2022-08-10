package com.smart.commons.jwt;

import com.smart.commons.jwt.claim.ClaimNames;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author ShiZhongMing
 * 2022/8/8 15:08
 * @since 1.0
 */
@EqualsAndHashCode
public class Jwt implements Serializable {

    @Serial
    private static final long serialVersionUID = 1057633528718217240L;
    @Getter
    private final String tokenValue;

    @Getter
    private final transient Map<String, Object> headers;

    @Getter
    private final transient Map<String, Object> claims;

    @Getter
    private final Instant issuedAt;

    @Getter
    private final Instant expiresAt;

    public Jwt(String tokenValue, Instant issuedAt, Instant expiresAt, Map<String, Object> headers, Map<String, Object> claims) {
        this.tokenValue = tokenValue;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.headers = headers;
        this.claims = claims;
    }

    public static Builder builder(String token) {
        return new Builder(token);
    }

    public static final class Builder {
        private final String tokenValue;

        private final Map<String, Object> claims = new LinkedHashMap<>();

        private final Map<String, Object> headers = new LinkedHashMap<>();

        private Builder(String tokenValue) {
            this.tokenValue = tokenValue;
        }

        public Builder claim(String name, Object value) {
            this.claims.put(name, value);
            return this;
        }

        public Builder claims(Consumer<Map<String, Object>> claimsConsumer) {
            claimsConsumer.accept(this.claims);
            return this;
        }

        public Builder header(String name, Object value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder headers(Consumer<Map<String, Object>> headersConsumer) {
            headersConsumer.accept(this.headers);
            return this;
        }

        public Builder expiresAt(Instant expiresAt) {
            this.claim(ClaimNames.EXP, expiresAt);
            return this;
        }

        public Builder jti(String jti) {
            this.claim(ClaimNames.JTI, jti);
            return this;
        }

        public Builder issuedAt(Instant issuedAt) {
            this.claim(ClaimNames.IAT, issuedAt);
            return this;
        }

        public Builder subject(String subject) {
            this.claim(ClaimNames.SUB, subject);
            return this;
        }

        public Jwt build() {
            return new Jwt(this.tokenValue, (Instant) this.claims.get(ClaimNames.IAT), (Instant) this.claims.get(ClaimNames.EXP), this.headers, this.claims);
        }
    }
}
