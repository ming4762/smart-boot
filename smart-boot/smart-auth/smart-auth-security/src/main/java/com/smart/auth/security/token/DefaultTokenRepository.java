package com.smart.auth.security.token;

import com.google.common.collect.Lists;
import com.smart.auth.core.constants.LoginTypeEnum;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.core.token.TokenData;
import com.smart.auth.core.token.TokenRepository;
import com.smart.auth.core.userdetails.RestUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认的token存储器
 * @author zhongming4762
 * 2023/3/6
 */
public class DefaultTokenRepository implements TokenRepository {

    private static final String JWT_SPLIT_KEY = "&#";

    private static final String TOKE_KEY_PREFIX = "jwt-token";

    private static final String DATA_KEY_PREFIX = "jwt-attribute";

    private final AuthProperties authProperties;

    private final AuthCache<String, Object> authCache;

    public DefaultTokenRepository(AuthProperties authProperties, AuthCache<String, Object> authCache) {
        this.authProperties = authProperties;
        this.authCache = authCache;
    }

    /**
     * 保存JWT
     *
     * @param token token
     * @param user  用户信息
     * @return 是否保存成功
     */
    @Override
    public boolean save(@NonNull String token, @NonNull RestUserDetails user) {
        LoginTypeEnum loginType = user.getLoginType();
        // 获取有效期
        Duration timeout = authProperties.getSession().getTimeout().getGlobal();
        if (Objects.equals(loginType, LoginTypeEnum.MOBILE)) {
            timeout = authProperties.getSession().getTimeout().getMobile();
        } else if (Objects.equals(loginType, LoginTypeEnum.REMEMBER)) {
            timeout = authProperties.getSession().getTimeout().getRemember();
        }
        // 保存jwt到cache中
        LocalDateTime currentTime = LocalDateTime.now();
        this.authCache.put(this.getTokenKey(user.getUsername(), token), new TokenData(token, currentTime, currentTime, timeout), timeout);
        return true;
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
        String tokenKey = this.getTokenKey(user.getUsername(), token);

        String attributeKey = this.getAttributeKey(user.getUsername(), token);
        // 获取有效期
        TokenData jwtData = (TokenData) this.authCache.get(tokenKey);
        if (jwtData != null) {
            jwtData.setRefreshTime(LocalDateTime.now());
            this.authCache.put(tokenKey, jwtData, jwtData.getTimeout());
            this.authCache.expire(attributeKey, jwtData.getTimeout());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过token失效
     *
     * @param username 用户名
     * @param token    token
     * @return 是否成功
     */
    @Override
    public boolean invalidateByToken(@NonNull String username, @NonNull String token) {
        this.authCache.remove(this.getTokenKey(username, token));
        this.authCache.remove(this.getAttributeKey(username, token));
        return true;
    }

    /**
     * 使用户登录失效
     *
     * @param username 用户名
     * @return 是否成功
     */
    @Override
    public boolean invalidateByUsername(@NonNull String username) {
        // 获取存储的key
        String matchTokenKey = this.getTokenKey(username, null);
        String matchAttributeKey = this.getAttributeKey(username, null);
        this.authCache.matchRemove(matchTokenKey);
        this.authCache.matchRemove(matchAttributeKey);
        return true;
    }

    /**
     * 查询所有JWT
     *
     * @return 所有jwt
     */
    @NonNull
    @Override
    public Set<String> listAll() {
        Set<String> keys = this.authCache.matchKeys(this.getTokenKey(null, null));
        if (CollectionUtils.isEmpty(keys)) {
            return new HashSet<>(0);
        }
        return keys.stream().map(item -> item.split(JWT_SPLIT_KEY)[2]).collect(Collectors.toSet());
    }

    /**
     * 通过用户名查询JWT
     *
     * @param username 用户名
     * @return jwt列表
     */
    @NonNull
    @Override
    public Set<String> listAll(@NonNull String username) {
        Set<String> keys = this.authCache.matchKeys(this.getTokenKey(username, null));
        if (CollectionUtils.isEmpty(keys)) {
            return new HashSet<>(0);
        }
        return keys.stream().map(item -> item.split(JWT_SPLIT_KEY)[2]).collect(Collectors.toSet());
    }

    /**
     * 查询所有数据
     *
     * @return jwt数据
     */
    @NonNull
    @Override
    public List<TokenData> listData() {
        Set<String> keys = this.authCache.matchKeys(this.getTokenKey(null, null));
        if (CollectionUtils.isEmpty(keys)) {
            return new ArrayList<>(0);
        }
        return this.authCache.batchGet(keys).stream()
                .map(TokenData.class::cast)
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
    public List<TokenData> listData(@NonNull String username) {
        Set<String> keys = this.authCache.matchKeys(this.getTokenKey(username, null));
        if (CollectionUtils.isEmpty(keys)) {
            return new ArrayList<>(0);
        }
        return this.authCache.batchGet(keys).stream()
                .map(TokenData.class::cast)
                .toList();
    }

    /**
     * 获取token的key
     * @param username 用户名
     * @return jst
     */
    @NonNull
    protected String getTokenKey(String username, String jwt) {
        List<String> list = Lists.newArrayList(TOKE_KEY_PREFIX, username, jwt)
                .stream().filter(StringUtils::isNotBlank)
                .toList();
        return String.join(JWT_SPLIT_KEY, list);
    }

    protected String getAttributeKey(String username, String jwt) {
        List<String> list = Lists.newArrayList(DATA_KEY_PREFIX, username, jwt)
                .stream().filter(StringUtils::isNotBlank)
                .toList();

        return String.join(JWT_SPLIT_KEY, list);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
