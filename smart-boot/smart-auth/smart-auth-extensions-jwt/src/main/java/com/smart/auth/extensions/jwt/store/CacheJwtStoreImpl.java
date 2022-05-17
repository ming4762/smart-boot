package com.smart.auth.extensions.jwt.store;

import com.google.common.collect.Lists;
import com.smart.auth.core.constants.LoginTypeEnum;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.extensions.jwt.data.JwtData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * jwt存储器 基于缓存实现
 * @author ShiZhongMing
 * 2022/1/17
 * @since 2.0.1
 */
public class CacheJwtStoreImpl implements CacheJwtStore {

    private static final String JWT_SPLIT_KEY = "&#";

    private static final String TOKE_KEY_PREFIX = "jwt-token";

    private static final String DATA_KEY_PREFIX = "jwt-attribute";

    private final AuthProperties authProperties;

    private final AuthCache<String, Object> authCache;

    public CacheJwtStoreImpl(AuthProperties authProperties, AuthCache<String, Object> authCache) {
        this.authProperties = authProperties;
        this.authCache = authCache;
    }


    @Override
    public boolean save(@NonNull String jwt, @NonNull RestUserDetails user) {
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
        this.authCache.put(this.getTokenKey(user.getUsername(), jwt), new JwtData(jwt, currentTime, currentTime, timeout), timeout);
        return true;
    }

    /**
     * 验证JWT
     * @param jwt jwt
     * @param user 用户信息
     * @return 验证结果
     */
    @Override
    public boolean validate(@NonNull String jwt, @NonNull RestUserDetails user) {
        String jwtKey = this.getTokenKey(user.getUsername(), jwt);

        String attributeKey = this.getAttributeKey(user.getUsername(), jwt);
        // 获取有效期
        JwtData jwtData = (JwtData) this.authCache.get(jwtKey);
        if (jwtData != null) {
            jwtData.setRefreshTime(LocalDateTime.now());
            this.authCache.put(jwtKey, jwtData, jwtData.getTimeout());
            this.authCache.expire(attributeKey, jwtData.getTimeout());
            return true;
        } else {
            return false;
        }
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
                .collect(Collectors.toList());
        return String.join(JWT_SPLIT_KEY, list);
    }

    protected String getAttributeKey(String username, String jwt) {
        List<String> list = Lists.newArrayList(DATA_KEY_PREFIX, username, jwt)
                .stream().filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        return String.join(JWT_SPLIT_KEY, list);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean invalidateByToken(@NonNull String username, @NonNull String token) {
        this.authCache.remove(this.getTokenKey(username, token));
        this.authCache.remove(this.getAttributeKey(username, token));
        return true;
    }

    @Override
    public boolean invalidateByUsername(@NonNull String username) {
        // 获取存储的key
        String matchTokenKey = this.getTokenKey(username, null);
        String matchAttributeKey = this.getAttributeKey(username, null);
        this.authCache.matchRemove(matchTokenKey);
        this.authCache.matchRemove(matchAttributeKey);
        return true;
    }

    @Override
    @NonNull
    public Set<String> listAll() {
        Set<String> keys = this.authCache.matchKeys(this.getTokenKey(null, null));
        if (CollectionUtils.isEmpty(keys)) {
            return new HashSet<>(0);
        }
        return keys.stream().map(item -> item.split(JWT_SPLIT_KEY)[2]).collect(Collectors.toSet());
    }

    @Override
    @NonNull
    public Set<String> listAll(@NonNull String username) {
        Set<String> keys = this.authCache.matchKeys(this.getTokenKey(username, null));
        if (CollectionUtils.isEmpty(keys)) {
            return new HashSet<>(0);
        }
        return keys.stream().map(item -> item.split(JWT_SPLIT_KEY)[2]).collect(Collectors.toSet());
    }

    @Override
    @NonNull
    public List<JwtData> listData() {
        Set<String> keys = this.authCache.matchKeys(this.getTokenKey(null, null));
        if (CollectionUtils.isEmpty(keys)) {
            return new ArrayList<>(0);
        }
        return this.authCache.batchGet(keys).stream()
                .map(JwtData.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public List<JwtData> listData(@NonNull String username) {
        Set<String> keys = this.authCache.matchKeys(this.getTokenKey(username, null));
        if (CollectionUtils.isEmpty(keys)) {
            return new ArrayList<>(0);
        }
        return this.authCache.batchGet(keys).stream()
                .map(JwtData.class::cast)
                .collect(Collectors.toList());
    }
}
