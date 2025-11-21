package com.smart.framework.auth.cache.redis.oauth2;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

/**
 * Redis OAuth2 Authorization Service
 * 用于缓存 OAuth2 授权信息
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/20 18:48
 * @since 5.0.0
 */
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    @Override
    public void save(OAuth2Authorization authorization) {

    }

    @Override
    public void remove(OAuth2Authorization authorization) {

    }


    @Override
    public OAuth2Authorization findById(String id) {
        return null;
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        return null;
    }
}
