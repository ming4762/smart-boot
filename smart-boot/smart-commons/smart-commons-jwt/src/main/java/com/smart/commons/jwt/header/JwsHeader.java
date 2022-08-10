package com.smart.commons.jwt.header;

import com.smart.commons.jwt.algorithm.JwaAlgorithm;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ShiZhongMing
 * 2022/8/8 16:00
 * @since 1.0
 */
public class JwsHeader {

    @Getter
    private final Map<String, Object> headers;

    private JwsHeader(Map<String, Object> headers) {
        Assert.notEmpty(headers, "headers cannot be empty");
        this.headers = Collections.unmodifiableMap(new HashMap<>(headers));
    }

    public <T extends JwaAlgorithm> T getAlgorithm() {
        return getHeader(HeaderNames.ALG);
    }

    public String getType() {
        return getHeader(HeaderNames.TYP);
    }

    public String getKeyId() {
        return getHeader(HeaderNames.KID);
    }

    public String getX509SHA256Thumbprint() {
        return getHeader(HeaderNames.X5T_S256);
    }

    public Map<String, Object> getJwk() {
        return getHeader(HeaderNames.JWK);
    }

    @SuppressWarnings("unchecked")
    public <T> T getHeader(@NonNull String name) {
        Assert.hasText(name, "name cannot be empty");
        return (T) this.headers.get(name);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder from(JwsHeader headers) {
        return new Builder(headers);
    }

    public static class Builder {
        private final Map<String, Object> headers = new HashMap<>();

        private Builder() {
            // do nothing
        }

        private Builder(JwsHeader headers) {
            Assert.notNull(headers, "headers cannot be null");
            this.headers.putAll(headers.getHeaders());
        }

        public Builder header(String name, Object value) {
            Assert.hasText(name, "name cannot be empty");
            Assert.notNull(value, "value cannot be null");
            this.headers.put(name, value);
            return Builder.this;
        }

        public Builder algorithm(JwaAlgorithm jwaAlgorithm) {
            return header(HeaderNames.ALG, jwaAlgorithm);
        }

        public Builder x509SHA256Thumbprint(String x509SHA256Thumbprint) {
            return header(HeaderNames.X5T_S256, x509SHA256Thumbprint);
        }

        public Builder type(String type) {
            return header(HeaderNames.TYP, type);
        }

        public Builder keyId(String keyId) {
            return header(HeaderNames.KID, keyId);
        }

        public Builder jwk(Map<String, Object> jwk) {
            return header(HeaderNames.JWK, jwk);
        }

        public JwsHeader build() {
            return new JwsHeader(this.headers);
        }

    }
}
