package com.smart.framework.auth.core.token;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.commons.core.dto.auth.AuthRole;
import com.smart.framework.commons.core.dto.auth.Permission;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * token信息
 * @author ShiZhongMing
 * 2022/4/12
 * @since 2.0.0
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Builder
public class TokenCacheData implements Serializable {

    @Serial
    private static final long serialVersionUID = -1673739084242479566L;

    public static final String TOKEN_KEY = "token";
    public static final String CREATE_TIME_KEY = "createTime";
    public static final String REFRESH_TIME_KEY = "refreshTime";
    public static final String TIMEOUT_KEY = "timeout";
    public static final String USER_KEY = "user";
    public static final String PERMISSIONS_KEY = "permissions";
    public static final String ROLES_KEY = "roles";
    public static final String ATTRIBUTES_KEY = "smartSessionAttr:";

    private String token;

    @Builder.Default
    private Instant createTime = Instant.now();

    private Instant refreshTime;

    private Duration timeout;

    private RestUserDetails user;
    /**
     * 权限信息
     */
    private Set<Permission> permissions;

    /**
     * 角色信息
     */
    private Set<AuthRole> roles;

    @Builder.Default
    private Map<String, Object> attributes = HashMap.newHashMap(0);


    public TokenCacheData(String token, Instant createTime, Instant refreshTime, Duration timeout, RestUserDetails user) {
        this.token = token;
        this.createTime = createTime;
        this.refreshTime = refreshTime;
        this.timeout = timeout;
        this.user = user;
    }

    /**
     * 判断是否过期
     * @return 是否过期
     */
    public boolean isExpired() {
        return Instant.now().isAfter(this.refreshTime.plus(this.timeout));
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = HashMap.newHashMap(attributes.size() + 7);
        map.put(TOKEN_KEY, this.token);
        map.put(CREATE_TIME_KEY, this.createTime);
        map.put(REFRESH_TIME_KEY, this.refreshTime);
        map.put(TIMEOUT_KEY, this.timeout);
        map.put(USER_KEY, this.user);
        map.put(PERMISSIONS_KEY, this.permissions);
        map.put(ROLES_KEY, this.roles);
        return map.entrySet().stream()
                .filter(item -> item.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Object> convertAttributesToMap() {
        Map<String, Object> map = HashMap.newHashMap(attributes.size());
        attributes.forEach((key, value) -> map.put(ATTRIBUTES_KEY + key, value));
        return map;
    }

    public static TokenCacheData createFormCache(Map<String, Object> cacheData) {
        TokenCacheData tokenCacheData = new TokenCacheData();
        tokenCacheData.setToken((String) cacheData.get(TOKEN_KEY));
        tokenCacheData.setCreateTime((Instant) cacheData.get(CREATE_TIME_KEY));
        tokenCacheData.setRefreshTime((Instant) cacheData.get(REFRESH_TIME_KEY));
        tokenCacheData.setTimeout((Duration) cacheData.get(TIMEOUT_KEY));
        tokenCacheData.setUser((RestUserDetails) cacheData.get(USER_KEY));
        tokenCacheData.setPermissions((Set<Permission>) cacheData.get(PERMISSIONS_KEY));
        tokenCacheData.setRoles((Set<AuthRole>) cacheData.get(ROLES_KEY));
        cacheData.forEach((key, value) -> {
            if (key.startsWith(ATTRIBUTES_KEY)) {
                tokenCacheData.getAttributes().put(key.substring(ATTRIBUTES_KEY.length()), value);
            }
        });
        return tokenCacheData;
    }

    public static String getAttributeKey(String attributeName) {
        return ATTRIBUTES_KEY + attributeName;
    }
}
