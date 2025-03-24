package com.smart.framework.auth.extensions.jwt.token;

import com.google.common.collect.Lists;
import com.smart.framework.auth.common.constants.LoginTypeEnum;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.core.token.TokenCacheData;
import com.smart.framework.auth.core.token.TokenRepository;
import com.smart.framework.auth.extensions.jwt.resolver.JwtResolver;
import com.smart.framework.commons.core.dto.auth.AuthRole;
import com.smart.framework.commons.core.dto.auth.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 默认的token存储器
 * @author zhongming4762
 * 2023/3/6
 */
@RequiredArgsConstructor
public class JwtTokenRepositoryOld implements TokenRepository {

    private static final String JWT_SPLIT_KEY = "&#";

    private static final String TOKE_KEY_PREFIX = "jwt-token";

    private static final String DATA_KEY_PREFIX = "jwt-attribute";

    private final AuthCache<Object> authCache;
    private final JwtResolver jwtResolver;
    private final boolean permissionCache;
    private final AuthProperties authProperties;

    public JwtTokenRepositoryOld(AuthProperties authProperties, AuthCache<Object> authCache, JwtResolver jwtResolver) {
        this.authCache = authCache;
        this.jwtResolver = jwtResolver;
        this.permissionCache = Boolean.TRUE.equals(authProperties.getJwt().getPermissionCache());
        this.authProperties = authProperties;
    }

    /**
     * 保存JWT
     *
     * @param user  用户信息
     * @return 是否保存成功
     */
    @Override
    public String save(@NonNull RestUserDetails user) {
        LoginTypeEnum loginType = user.getLoginType();
        // 获取有效期
        Duration timeout = authProperties.getSession().getTimeout().getGlobal();
        if (Objects.equals(loginType, LoginTypeEnum.MOBILE)) {
            timeout = authProperties.getSession().getTimeout().getMobile();
        } else if (Objects.equals(loginType, LoginTypeEnum.REMEMBER)) {
            timeout = authProperties.getSession().getTimeout().getRemember();
        }
        // 保存jwt到cache中
        Instant currentTime = Instant.now();
        String token = this.jwtResolver.create(user, null, null);

        TokenCacheData tokenData = new TokenCacheData(token, currentTime, currentTime, timeout, user);
        if (Boolean.TRUE.equals(this.permissionCache)) {
            Set<Permission> permissions = user.getPermissions();
            Set<AuthRole> roles = user.getRoles();
//            tokenData.setRoles(roles);
//            tokenData.setPermissions(permissions);
        }
//        this.authCache.putMap(this.getTokenKey(user.getUsername(), user.getUserTenant().getTenantId(), token), tokenData, timeout);
        return token;
    }

    /**
     * 验证JWT
     *
     * @param token token
     * @param user  用户信息
     * @return 验证结果
     */
    @Override
    public boolean validate(@NonNull String token, @NonNull RestUserDetails user) {
        String tokenKey = this.getTokenKey(user.getUsername(), user.getUserTenant().getTenantId(), token);

        String attributeKey = this.getAttributeKey(user.getUsername(), user.getUserTenant().getTenantId(), token);
        // 获取有效期
        TokenCacheData jwtData = (TokenCacheData) this.authCache.get(tokenKey);
        if (jwtData != null) {
            jwtData.setRefreshTime(Instant.now());
//            this.authCache.putMap(tokenKey, jwtData, jwtData.getTimeout());
            this.authCache.expire(attributeKey, jwtData.getTimeout());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过token失效
     * @param token    token
     * @return 是否成功
     */
    @Override
    public boolean invalidateByToken(@NonNull String token) {
        RestUserDetails user = this.jwtResolver.resolver(token);
        Long tenantId = user.getUserTenant().getTenantId();
        this.authCache.remove(this.getTokenKey(user.getUsername(), tenantId, token));
        this.authCache.remove(this.getAttributeKey(user.getUsername(), tenantId, token));
        return true;
    }

    /**
     * 使用户登录失效
     *
     * @param username 用户名
     * @return 是否成功
     */
    @Override
    public boolean invalidateByUsername(@NonNull Long tenantId, @NonNull String username) {
        // 获取存储的key
        String matchTokenKey = this.getTokenKey(username, tenantId, null);
        String matchAttributeKey = this.getAttributeKey(username, tenantId, null);
        this.authCache.matchRemove(matchTokenKey);
        this.authCache.matchRemove(matchAttributeKey);
        return true;
    }

    /**
     * 查询所有数据
     *
     * @return jwt数据
     */
    @NonNull
    @Override
    public List<TokenCacheData> listData() {
        Set<String> keys = this.authCache.matchKeys(this.getTokenKey(null, null, null));
        if (CollectionUtils.isEmpty(keys)) {
            return new ArrayList<>(0);
        }
        return this.authCache.batchGet(keys).stream()
                .map(TokenCacheData.class::cast)
                .toList();
    }

    /**
     * 通过用户名查询jwt数据
     *
     * @param username 用户名
     * @return jwt数据
     */
    @NonNull
    @Override
    public List<TokenCacheData> listData(@NonNull String username, @NonNull Long tenantId) {
        Set<String> keys = this.authCache.matchKeys(this.getTokenKey(username, tenantId, null));
        if (CollectionUtils.isEmpty(keys)) {
            return new ArrayList<>(0);
        }
        return this.authCache.batchGet(keys).stream()
                .map(TokenCacheData.class::cast)
                .toList();
    }

    /**
     * 获取token数据
     *
     * @param token token
     * @return TokenData
     */
    @Override
    public TokenCacheData getData(String token) {
        RestUserDetails user = this.jwtResolver.resolver(token);
        TokenCacheData tokenData = (TokenCacheData) this.authCache.get(this.getTokenKey(user.getUsername(), user.getUserTenant().getTenantId(), token));
        if (tokenData == null) {
            return null;
        }
        tokenData.setUser(user);
        return tokenData;
    }

    /**
     * 获取token的key
     * @param tenantId 租户ID
     * @param username 用户名
     * @return jst
     */
    @NonNull
    protected String getTokenKey(String username, Long tenantId, String jwt) {
        List<String> list = Lists.newArrayList(TOKE_KEY_PREFIX, username, tenantId, jwt)
                .stream().filter(Objects::nonNull)
                .map(Object::toString)
                .toList();
        return String.join(JWT_SPLIT_KEY, list);
    }

    protected String getAttributeKey(String username, Long tenantId, String jwt) {
        List<String> list = Lists.newArrayList(DATA_KEY_PREFIX, username, tenantId, jwt)
                .stream().filter(Objects::nonNull)
                .map(Object::toString)
                .toList();

        return String.join(JWT_SPLIT_KEY, list);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 通过TOKEN获取用户信息
     *
     * @param token token
     * @return 用户信息
     */
    @Override
    public RestUserDetails getUser(String token) {
        TokenCacheData tokenData = this.getData(token);
        return this.getUserFromCacheData(tokenData);
    }

    private RestUserDetails getUserFromCacheData(TokenCacheData tokenData) {
        if (tokenData == null) {
            return null;
        }
        RestUserDetails user = tokenData.getUser();
        if (Boolean.TRUE.equals(this.permissionCache)) {
//            Set<SmartGrantedAuthority> authorities = HashSet.newHashSet(tokenData.getPermissions().size() + tokenData.getRoles().size());
            // 添加权限信息
//            tokenData.getPermissions().forEach(permission -> authorities.add(new PermissionGrantedAuthority(permission)));
//             添加角色信息
//            tokenData.getRoles().forEach(item -> authorities.add(new RoleGrantedAuthority(item)));
//            ((RestUserDetailsImpl) user).setAuthorities(authorities);
        }
        return user;
    }

}
