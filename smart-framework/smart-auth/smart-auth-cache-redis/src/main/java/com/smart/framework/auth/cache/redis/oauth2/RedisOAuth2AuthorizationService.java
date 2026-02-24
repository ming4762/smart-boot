package com.smart.framework.auth.cache.redis.oauth2;

import com.smart.framework.auth.cache.redis.RedisAuthCache;
import com.smart.framework.auth.core.properties.AuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Optional;

/**
 * Redis OAuth2 Authorization Service
 * 用于缓存 OAuth2 授权信息
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/20 18:48
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private static final String OAUTH2_AUTHORIZATION_ID_KEY = "sso:oauth2:authorization:id";
    private static final String OAUTH2_AUTHORIZATION_ACCESS_TOKEN_KEY = "sso:oauth2:authorization:access_token";
    private static final String OAUTH2_AUTHORIZATION_REFRESH_TOKEN_KEY = "sso:oauth2:authorization:refresh_token";


    private final AuthProperties authProperties;
    private final RedisAuthCache redisAuthCache;

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        String idCachedKey = this.getIdCachedKey(authorization);
        // TODO: timeout独立配置
        Duration timeout = this.authProperties.getSession().getTimeout().getRemember();
        this.redisAuthCache.put(idCachedKey, authorization, timeout);
        if (authorization.getAccessToken() != null) {
            String tokenCachedKey = this.getTokenCachedKey(authorization.getAccessToken().getToken().getTokenValue(), OAuth2TokenType.ACCESS_TOKEN);
            this.redisAuthCache.put(tokenCachedKey, authorization.getId(), timeout);
        }
        OAuth2RefreshToken oAuth2RefreshToken = Optional.ofNullable(authorization.getRefreshToken())
                .map(OAuth2Authorization.Token::getToken)
                .orElse(null);
        if (oAuth2RefreshToken != null) {
            String tokenCachedKey = this.getTokenCachedKey(oAuth2RefreshToken.getTokenValue(), OAuth2TokenType.REFRESH_TOKEN);
            this.redisAuthCache.put(tokenCachedKey, authorization.getId(), timeout);
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        String idCachedKey = this.getIdCachedKey(authorization);
        this.redisAuthCache.remove(idCachedKey);
        if (authorization.getAccessToken() != null) {
            String tokenCachedKey = this.getTokenCachedKey(authorization.getAccessToken().getToken().getTokenValue(), OAuth2TokenType.ACCESS_TOKEN);
            this.redisAuthCache.remove(tokenCachedKey);
        }
        OAuth2RefreshToken oAuth2RefreshToken = Optional.ofNullable(authorization.getRefreshToken())
                .map(OAuth2Authorization.Token::getToken)
                .orElse(null);
        if (oAuth2RefreshToken != null) {
            String tokenCachedKey = this.getTokenCachedKey(oAuth2RefreshToken.getTokenValue(), OAuth2TokenType.REFRESH_TOKEN);
            this.redisAuthCache.remove(tokenCachedKey);
        }
    }


    @Override
    public OAuth2Authorization findById(String id) {
        String idCachedKey = this.getIdCachedKey(id);
        return this.redisAuthCache.getValue(idCachedKey);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        String tokenCachedKey = this.getTokenCachedKey(token, tokenType);
        String id = this.redisAuthCache.getValue(tokenCachedKey);
        return this.findById(id);
    }

    private String getIdCachedKey(OAuth2Authorization authorization) {
        return this.getIdCachedKey(authorization.getId());
    }
    private String getIdCachedKey(String id) {
        return OAUTH2_AUTHORIZATION_ID_KEY + ":" + id;
    }
    private String getTokenCachedKey(String token, OAuth2TokenType tokenType) {
        if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            return OAUTH2_AUTHORIZATION_ACCESS_TOKEN_KEY + ":" + token;
        }
        if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            return OAUTH2_AUTHORIZATION_REFRESH_TOKEN_KEY + ":" + token;
        }
        throw new IllegalArgumentException("tokenType must be access_token or refresh_token");
    }

}
