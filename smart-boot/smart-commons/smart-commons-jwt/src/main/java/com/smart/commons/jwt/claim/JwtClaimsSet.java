package com.smart.commons.jwt.claim;

import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ShiZhongMing
 * 2022/8/8
 * @since 3.0.0
 */
public class JwtClaimsSet {

    @Getter
    private final Map<String, Object> claims;

    private JwtClaimsSet(Map<String, Object> claims) {
        this.claims = Collections.unmodifiableMap(new HashMap<>(claims));
    }

    public String getSubject() {
        return getClaims(ClaimNames.SUB);
    }

    public Instant getExpiresAt() {
        return getClaims(ClaimNames.EXP);
    }

    public Instant getIssuedAt() {
        return getClaims(ClaimNames.IAT);
    }

    public String getId() {
        return getClaims(ClaimNames.JTI);
    }

    @SuppressWarnings("unchecked")
    public <T> T getClaims(@NonNull String name) {
        Assert.hasText(name, "name cannot be empty");
        return (T) this.claims.get(name);
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<String, Object> claims = new HashMap<>();

        private Builder() {
        }

        public Builder claim(String name, Object value) {
            Assert.hasText(name, "name cannot be empty");
            Assert.notNull(value, "value cannot be null");
            this.claims.put(name, value);
            return this;
        }


        public Builder subject(String subject) {
            return claim(ClaimNames.SUB, subject);
        }

        public Builder expiresAt(Instant expiresAt) {
            return claim(ClaimNames.EXP, expiresAt);
        }

        public Builder issuedAt(Instant issuedAt) {
            return claim(ClaimNames.IAT, issuedAt);
        }

        public Builder id(String jti) {
            return claim(ClaimNames.JTI, jti);
        }

        public JwtClaimsSet build() {
            Assert.notEmpty(this.claims, "claims cannot be empty");
            return new JwtClaimsSet(this.claims);
        }
    }
}
