package com.smart.auth.security.remember;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import lombok.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.util.Date;
import java.util.Set;

/**
 * @author shizhongming
 * 2024/5/22 16:10
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class AuthCachePersistentTokenRepository implements PersistentTokenRepository {

    private static final String CACHE_KEY_PREFIX = "remember-me:";

    private final AuthCache<String, Object> authCache;
    private final AuthProperties authProperties;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        String keyForSeries = this.getKeyForSeries(token.getSeries());
        PersistentRememberMeTokenSerializable current = (PersistentRememberMeTokenSerializable) this.authCache.get(keyForSeries);
        if (current != null) {
            throw new DataIntegrityViolationException("Series Id '" + token.getSeries() + "' already exists!");
        }
        PersistentRememberMeTokenSerializable serializableToken = new PersistentRememberMeTokenSerializable(
                token.getUsername(),
                token.getSeries(),
                token.getTokenValue(),
                token.getDate()
        );
        this.authCache.put(keyForSeries, serializableToken, this.getTimeout());
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        PersistentRememberMeToken token = this.getTokenForSeries(series);
        PersistentRememberMeTokenSerializable newToken = new PersistentRememberMeTokenSerializable(token.getUsername(), series, tokenValue, new Date());
        this.authCache.put(this.getKeyForSeries(series), newToken, this.getTimeout());
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        PersistentRememberMeTokenSerializable serializable = (PersistentRememberMeTokenSerializable) this.authCache.get(this.getKeyForSeries(seriesId));
        if (serializable == null) {
            return null;
        }
        return new PersistentRememberMeToken(serializable.getUsername(), serializable.getSeries(), serializable.getTokenValue(), serializable.getDate());
    }

    @Override
    public void removeUserTokens(String username) {
        Set<Object> cacheList = this.authCache.matchGet(CACHE_KEY_PREFIX);
        if (CollectionUtils.isEmpty(cacheList)) {
            return;
        }
        cacheList.forEach(item -> {
            PersistentRememberMeTokenSerializable token = (PersistentRememberMeTokenSerializable) item;
            if (token.getUsername().equals(username)) {
                this.authCache.remove(this.getKeyForSeries(token.getSeries()));
            }
        });
    }

    protected String getKeyForSeries(String seriesId) {
        return CACHE_KEY_PREFIX + seriesId;
    }

    private Duration getTimeout() {
        return this.authProperties.getSession().getTimeout().getRemember();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class PersistentRememberMeTokenSerializable implements Serializable {

        @Serial
        private static final long serialVersionUID = 8058089973474947886L;

        private String username;

        private String series;

        private String tokenValue;

        private Date date;
    }
}
