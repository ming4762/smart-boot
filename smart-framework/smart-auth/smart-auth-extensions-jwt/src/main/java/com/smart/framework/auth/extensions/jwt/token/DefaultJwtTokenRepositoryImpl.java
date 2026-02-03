package com.smart.framework.auth.extensions.jwt.token;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.auth.core.model.PermissionGrantedAuthority;
import com.smart.framework.auth.core.model.RestUserDetailsImpl;
import com.smart.framework.auth.core.model.RoleGrantedAuthority;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.service.AbstractAuthCache;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.core.token.TokenCacheData;
import com.smart.framework.auth.extensions.jwt.resolver.JwtResolver;
import com.smart.framework.commons.core.dto.auth.AuthRole;
import com.smart.framework.commons.core.dto.auth.Permission;
import com.smart.framework.commons.core.i18n.I18nUtils;
import com.smart.framework.commons.core.utils.BeanUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * jwt token存储器
 * @author shizhongming
 * 2025/3/23 19:18
 * @since 5.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class DefaultJwtTokenRepositoryImpl implements JwtTokenRepository {

    private static final String JWT_PERMISSION_KEY = "permissions";
    private static final String JWT_ROLE_KEY = "roles";
    private static final String JWT_AUTHORITIES = "authorities";

    private static final String TOKE_KEY_PREFIX = "jwt-token";
    private static final String REFRESH_TOKEN_KEY_PREFIX = "jwt-refresh-token";
    private static final String CACHED_TOKEN_LIST = "token-list";

    private final AuthProperties authProperties;
    private final JwtResolver jwtResolver;
    private final AuthCache<Object> authCache;

    /**
     * 生成token
     *
     * @return token
     */
    @Override
    public String generateToken() {
        throw new UnsupportedOperationException();
    }

    /**
     * 生成token
     *
     * @param userDetails 用户信息
     * @return token
     */
    @Override
    public String generateToken(@NonNull RestUserDetails userDetails) {
        RestUserDetailsImpl user = (RestUserDetailsImpl) userDetails;
        Duration timeout = this.authProperties.getSession().getTimeout().getGlobal();
        // 生成refresh token
        Duration refreshTimeout = this.authProperties.getSession().getTimeout().getRemember();
        String refreshToken = this.jwtResolver.create(user.getUserId(), refreshTimeout, JwtRefreshTokenPayload.createByUser(user));
        String jwt;
        user.setRefreshToken(refreshToken);
        if (user.isPermissionCache()) {
            // 如果缓存则移除角色和权限信息
            Map<String, Object> userMap = BeanUtils.beanToMap(user);
            userMap.remove(JWT_PERMISSION_KEY);
            userMap.remove(JWT_ROLE_KEY);
            userMap.remove(JWT_AUTHORITIES);
            jwt = this.jwtResolver.create(user.getUserId(), timeout, userMap);
            // 存储缓存数据
            String cachedKey = this.getTokenKey(user.getUsername(), user.getUserTenant().getTenantId(), jwt);
            this.authCache.putAll(
                    cachedKey,
                    Map.of(
                            JWT_PERMISSION_KEY, user.getPermissions(),
                            JWT_ROLE_KEY, user.getRoles()
                    ),
                    timeout
            );
        } else {
            jwt = this.jwtResolver.create(user.getUserId(), timeout, user);
        }
        user.setToken(jwt);
        // 保存refresh token负载到缓存
        this.cachedRefreshToken(user, refreshToken, refreshTimeout);
        return jwt;
    }

    /**
     * 缓存refresh token负载
     * @param user 用户信息
     * @param refreshToken refresh token
     * @param timeout 超时时间
     */
    private void cachedRefreshToken(RestUserDetails user, String refreshToken, Duration timeout) {
        TokenCacheData tokenCacheData = TokenCacheData.builder()
                .timeout(timeout)
                .user(user)
                .token(refreshToken)
                .refreshTime(Instant.now())
                .build();
        String refreshTokenKey = this.getRefreshTokenKey(user.getUsername(), user.getUserTenant().getTenantId(), refreshToken);
        this.authCache.putAll(
                refreshTokenKey,
                tokenCacheData.convertAllToMap(),
                timeout
        );
        this.cacheRefreshTokenAttribute(refreshTokenKey, CACHED_TOKEN_LIST, List.of(user.getToken()));
    }

    /**
     * 缓存refresh token 额外负载
     * @param refreshTokenKey refresh token key
     * @param attributeKey 属性key
     * @param attributeValue 属性值
     */
    private void cacheRefreshTokenAttribute(String refreshTokenKey, String attributeKey, Object attributeValue) {
        this.authCache.putMap(refreshTokenKey, TokenCacheData.getAttributeKey(attributeKey), attributeValue);
    }

    /**
     * 通过refreshToken申请token
     *
     * @param refreshToken refreshToken
     * @return token
     */
    @Override
    public String applyToken(String refreshToken) {
        TokenCacheData tokenCacheData = this.getRefreshTokenCacheData(refreshToken);
        RestUserDetails cachedUser = tokenCacheData.getUser();
        // 生成新tokenTOKEN
        String newToken = this.generateToken(cachedUser);
        // 移除原token缓存
        if (cachedUser.isPermissionCache()) {
            List<String> tokenList = (List<String>) tokenCacheData.getAttributes().get(CACHED_TOKEN_LIST);
            if (!CollectionUtils.isEmpty(tokenList)) {
                tokenList.forEach(item -> this.authCache.remove(this.getTokenKey(cachedUser.getUsername(), cachedUser.getUserTenant().getTenantId(), item)));
            }
        }
        String refreshTokenKey = this.getRefreshTokenKey(cachedUser.getUsername(), cachedUser.getUserTenant().getTenantId(), refreshToken);
        this.authCache.putMap(refreshTokenKey, CACHED_TOKEN_LIST, List.of(newToken));
        // 设置新的token
        return newToken;
    }

    /**
     * 获取refresh token缓存数据
     * @param refreshToken refresh token
     * @return refresh token缓存数据
     */
    private TokenCacheData getRefreshTokenCacheData(String refreshToken) {
        // 解析refresh token
        JwtRefreshTokenPayload userData = this.jwtResolver.resolverRefreshToken(refreshToken);
        if (userData == null) {
            // 刷新token已过期
            log.warn("Refresh token expired, refreshToken: {}", refreshToken);
            throw new CredentialsExpiredException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EXPIRE));
        }
        // 获取缓存的用户数据
        String refreshTokenKey = this.getRefreshTokenKey(userData.username(), userData.userTenant().getTenantId(), refreshToken);
        Map<String, Object> cachedData = this.authCache.get(refreshTokenKey);
        if (CollectionUtils.isEmpty(cachedData)) {
            throw new CredentialsExpiredException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EXPIRE));
        }
        return TokenCacheData.createFormCache(cachedData);
    }

    /**
     * 查询所有数据
     *
     * @return jwt数据
     */
    @Override
    @NonNull
    public List<TokenCacheData> listToken() {
        String refreshTokenKey = this.getRefreshTokenKey(null, null, null);
        return this.listTokenKey(refreshTokenKey);
    }

    private List<TokenCacheData> listTokenKey(String cachedKey) {
        Set<String> keys = this.authCache.matchKeys(cachedKey);
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        return this.authCache.batchGet(keys).stream()
                .map(TokenCacheData::createFormCache)
                .toList();
    }

    /**
     * 通过用户名查询token
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return token
     */
    @Override
    @NonNull
    public List<TokenCacheData> listToken(String username, Long tenantId) {
        return this.listTokenKey(this.getRefreshTokenKey(username, tenantId, null));
    }

    /**
     * 获取用户缓存数据
     *
     * @param attributeName 属性名称
     * @return 属性值
     */
    @Override
    public <T> T getAttribute(String attributeName) {
        return null;
    }

    /**
     * 设置用户缓存数据
     *
     * @param attributeName  属性名称
     * @param attributeValue 属性值
     */
    @Override
    public boolean setAttribute(String attributeName, Object attributeValue) {
        return false;
    }

    /**
     * 使token失效
     * 这里虽然函数命名是invalidateByToken，实际入参是refresh token
     * 因为jwt模式，token并没有存储在缓存中，所以这里失效的是refresh token
     *
     * @param token token
     * @return 是否失效成功
     */
    @Override
    public boolean invalidateByToken(String token) {
        TokenCacheData refreshTokenCacheData = this.getRefreshTokenCacheData(token);
        List<String> tokenList = (List<String>) refreshTokenCacheData.getAttributes().get(CACHED_TOKEN_LIST);
        if (!CollectionUtils.isEmpty(tokenList)) {
            tokenList.forEach(this::doInvalidateByToken);
        }
        return true;
    }

    /**
     * 使token失效
     * @param token token
     * @return 是否失效成功
     */
    protected boolean doInvalidateByToken(String token) {
        RestUserDetails userDetails = this.jwtResolver.resolverToken(token);
        if (userDetails == null) {
            return false;
        }
        // 移除刷新token缓存
        String refreshTokenKey = this.getRefreshTokenKey(userDetails.getUsername(), userDetails.getUserTenant().getTenantId(), userDetails.getRefreshToken());
        this.authCache.remove(refreshTokenKey);
        if (!userDetails.isPermissionCache()) {
            // 没有缓存数据，直接返回
            return true;
        }
        String cachedKey = this.getTokenKey(userDetails.getUsername(), userDetails.getUserTenant().getTenantId(), token);
        this.authCache.remove(cachedKey);
        return true;
    }

    /**
     * 使用户登录失效
     * 永远返回false，true会中断后续的token失效
     * @param tenantId 租户ID
     * @param username 用户名
     * @return 是否失效成功
     */
    @Override
    public boolean invalidateByUsername(Long tenantId, String username) {
        String cachedKey = this.getRefreshTokenKey(username, tenantId, null);
        Set<String> keys = this.authCache.matchKeys(cachedKey);
        if (CollectionUtils.isEmpty(keys)) {
            return false;
        }
        keys.forEach(key -> this.invalidateByToken(this.getTokenFromCachedKey(key)));
        return false;
    }

    /**
     * 通过token获取用户信息
     *
     * @param token token
     * @return 用户信息
     */
    @Override
    public RestUserDetails getUserByToken(String token) {
        RestUserDetailsImpl userDetails = (RestUserDetailsImpl) this.jwtResolver.resolverToken(token);
        if (userDetails == null) {
            return null;
        }
        // 注入权限角色信息
        if (!userDetails.isPermissionCache()) {
            return userDetails;
        }
        String cachedKey = this.getTokenKey(userDetails.getUsername(), userDetails.getUserTenant().getTenantId(), token);
        // 添加权限信息
        Set<Permission> permissionList = (Set<Permission>) this.authCache.get(cachedKey, JWT_PERMISSION_KEY);
        if (!CollectionUtils.isEmpty(permissionList)) {
            userDetails.setPermissions(
                    permissionList.stream().map(PermissionGrantedAuthority::new).collect(Collectors.toSet())
            );
        }
        // 添加角色信息
        Set<AuthRole> roleList = (Set<AuthRole>) this.authCache.get(cachedKey, JWT_ROLE_KEY);
        if (!CollectionUtils.isEmpty(roleList)) {
            userDetails.setRoles(
                    roleList.stream().map(RoleGrantedAuthority::new).collect(Collectors.toSet())
            );
        }
        return userDetails;
    }

    private String getTokenKey(String username, Long tenantId, String token) {
        return this.getCachedKey(TOKE_KEY_PREFIX, username, tenantId, token);
    }

    private String getRefreshTokenKey(String username, Long tenantId, String token) {
        return this.getCachedKey(REFRESH_TOKEN_KEY_PREFIX, username, tenantId, token);
    }

    /**
     * 获取缓存key
     * @param username 用户名
     * @param tenantId 租户ID
     * @param token token
     * @return 缓存key
     */
    private String getCachedKey(String prefix, String username, Long tenantId, String token) {
        return Stream.of(prefix, username, tenantId, token)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.joining(AbstractAuthCache.SPLIT));
    }

    private String getTokenFromCachedKey(String cachedKey) {
        String[] split = cachedKey.split(AbstractAuthCache.SPLIT);
        return split[split.length - 1];
    }
}
