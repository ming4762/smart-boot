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
import com.smart.framework.commons.core.dto.auth.UserTenantDTO;
import com.smart.framework.commons.core.i18n.I18nUtils;
import com.smart.framework.commons.core.utils.BeanUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * jwt token存储器
 * @author shizhongming
 * 2025/3/23 19:18
 * @since 5.0.0
 */
@RequiredArgsConstructor
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
        Duration timeout = this.authProperties.getSession().getTimeout().getGlobal();
        String jwt;
        if (this.isPermissionCache()) {
            // 如果缓存则移除角色和权限信息
            Map<String, Object> userMap = BeanUtils.beanToMap(userDetails);
            userMap.remove(JWT_PERMISSION_KEY);
            userMap.remove(JWT_ROLE_KEY);
            userMap.remove(JWT_AUTHORITIES);
            jwt = this.jwtResolver.create(userDetails, timeout, userMap);
            // 存储缓存数据
            String cachedKey = this.getTokenKey(userDetails.getUsername(), userDetails.getUserTenant().getTenantId(), jwt);
            this.authCache.putAll(
                    cachedKey,
                    Map.of(
                            JWT_PERMISSION_KEY, userDetails.getPermissions(),
                            JWT_ROLE_KEY, userDetails.getRoles()
                    ),
                    timeout
            );
        } else {
            jwt = this.jwtResolver.create(userDetails, timeout, userDetails);
        }
        return jwt;
    }

    /**
     * 保存刷新token
     * @param user 用户信息
     * @return 是否保存成功
     */
    @Override
    public String generateRefreshToken(RestUserDetails user) {
        Duration timeout = this.authProperties.getSession().getTimeout().getRemember();

        String refreshToken = this.jwtResolver.create(user, timeout, RefreshTokenPayload.createByUser(user));
        TokenCacheData tokenCacheData = TokenCacheData.builder()
                .timeout(timeout)
                .user(user)
                .token(refreshToken)
                .build();
        String refreshTokenKey = this.getRefreshTokenKey(user.getUsername(), user.getUserTenant().getTenantId(), refreshToken);
        this.authCache.putAll(
                refreshTokenKey,
                tokenCacheData.convertAllToMap(),
                timeout
        );
        // 将refresh token对应的token存入缓存
        this.authCache.putMap(refreshTokenKey, CACHED_TOKEN_LIST, List.of(user.getToken()));
        return refreshToken;
    }

    /**
     * 通过refreshToken申请token
     *
     * @param refreshToken refreshToken
     * @return token
     */
    @Override
    public String applyToken(String refreshToken) {
        // 解析refresh token
        RestUserDetails userDetails = this.jwtResolver.resolver(refreshToken);
        // 获取缓存的用户数据
        String refreshTokenKey = this.getRefreshTokenKey(userDetails.getUsername(), userDetails.getUserTenant().getTenantId(), refreshToken);
        Map<String, Object> cachedData = this.authCache.get(refreshTokenKey);
        if (cachedData == null) {
            throw new CredentialsExpiredException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EXPIRE));
        }
        TokenCacheData tokenCacheData = TokenCacheData.createFormCache(cachedData);
        RestUserDetails cachedUser = tokenCacheData.getUser();
        // 生成新tokenTOKEN
        String newToken = this.generateToken(cachedUser);
        // 移除原token缓存
        if (this.isPermissionCache()) {
            List<String> tokenList = (List<String>) cachedData.get(CACHED_TOKEN_LIST);
            if (!CollectionUtils.isEmpty(tokenList)) {
                tokenList.forEach(item -> this.authCache.remove(this.getTokenKey(cachedUser.getUsername(), cachedUser.getUserTenant().getTenantId(), item)));
            }
        }
        this.authCache.putMap(refreshTokenKey, CACHED_TOKEN_LIST, List.of(newToken));
        // 设置新的token
        return newToken;
    }

    /**
     * 查询所有数据
     *
     * @return jwt数据
     */
    @Override
    public List<TokenCacheData> listToken() {
        return List.of();
    }

    /**
     * 通过用户名查询token
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return token
     */
    @Override
    public List<TokenCacheData> listToken(String username, Long tenantId) {
        return List.of();
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
    public void setAttribute(String attributeName, Object attributeValue) {

    }

    /**
     * 使token失效
     *
     * @param refreshToken 刷新token
     * @return 是否失效成功
     */
    @Override
    public boolean invalidateByToken(String refreshToken) {
        RestUserDetails userDetails = this.jwtResolver.resolver(refreshToken);
        String cachedKey = this.getRefreshTokenKey(userDetails.getUsername(), userDetails.getUserTenant().getTenantId(), refreshToken);
        if (this.isPermissionCache()) {
            // 清除token缓存
            List<String> tokenList = (List<String>) this.authCache.get(cachedKey, CACHED_TOKEN_LIST);
            if (!CollectionUtils.isEmpty(tokenList)) {
                tokenList.forEach(token -> this.authCache.remove(this.getTokenKey(userDetails.getUsername(), userDetails.getUserTenant().getTenantId(), token)));
            }
        }
        // 清除refresh token
        this.authCache.remove(cachedKey);
        return true;
    }

    /**
     * 使用户登录失效
     *
     * @param tenantId 租户ID
     * @param username 用户名
     * @return 是否失效成功
     */
    @Override
    public boolean invalidateByUsername(Long tenantId, String username) {
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
        RestUserDetailsImpl userDetails = (RestUserDetailsImpl) this.jwtResolver.resolver(token);
        // 注入权限角色信息
        if (!this.isPermissionCache()) {
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

    private boolean isPermissionCache() {
        return Boolean.TRUE.equals(this.authProperties.getJwt().getPermissionCache());
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

    private record RefreshTokenPayload(String username, UserTenantDTO userTenant) {
        static RefreshTokenPayload createByUser(RestUserDetails user) {
            return new RefreshTokenPayload(user.getUsername(), user.getUserTenant());
        }
    }
}
