package com.smart.framework.auth.core.token;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import lombok.*;
import org.springframework.security.core.context.SecurityContext;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
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
    public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private String token;

    @Builder.Default
    private Instant createTime = Instant.now();

    private Instant refreshTime;

    private Duration timeout;

    private RestUserDetails user;

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
        Map<String, Object> map = HashMap.newHashMap(5);
        map.put(TOKEN_KEY, this.token);
        map.put(CREATE_TIME_KEY, this.createTime);
        map.put(REFRESH_TIME_KEY, this.refreshTime);
        map.put(TIMEOUT_KEY, this.timeout);
        map.put(USER_KEY, this.user);
        return map.entrySet().stream()
                .filter(item -> item.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Object> convertAttributesToMap() {
        Map<String, Object> map = HashMap.newHashMap(attributes.size());
        attributes.forEach((key, value) -> map.put(ATTRIBUTES_KEY + key, value));
        return map;
    }

    /**
     * 所有数据转为map
     * @return map
     */
    public Map<String, Object> convertAllToMap() {
        Map<String, Object> map = HashMap.newHashMap(attributes.size() + 5);
        map.putAll(convertToMap());
        map.putAll(convertAttributesToMap());
        return map;
    }

    public static TokenCacheData createFormCache(Map<String, Object> cacheData) {
        TokenCacheData tokenCacheData = new TokenCacheData();
        tokenCacheData.setToken((String) cacheData.get(TOKEN_KEY));
        tokenCacheData.setCreateTime((Instant) cacheData.get(CREATE_TIME_KEY));
        tokenCacheData.setRefreshTime((Instant) cacheData.get(REFRESH_TIME_KEY));
        tokenCacheData.setTimeout((Duration) cacheData.get(TIMEOUT_KEY));
        if (cacheData.containsKey(USER_KEY)) {
            tokenCacheData.setUser((RestUserDetails) cacheData.get(USER_KEY));
        } else {
            tokenCacheData.setUser(getFromCacheData(cacheData));
        }
        cacheData.forEach((key, value) -> {
            if (key.startsWith(ATTRIBUTES_KEY)) {
                tokenCacheData.getAttributes().put(key.substring(ATTRIBUTES_KEY.length()), value);
            }
        });
        return tokenCacheData;
    }

    private static RestUserDetails getFromCacheData(Map<String, Object> cacheData) {
        SecurityContext securityContext = (SecurityContext) cacheData.get(ATTRIBUTES_KEY + SPRING_SECURITY_CONTEXT);
        if (securityContext == null) {
            return null;
        }
        return (RestUserDetails) securityContext.getAuthentication().getPrincipal();
    }

    public static String getAttributeKey(String attributeName) {
        return ATTRIBUTES_KEY + attributeName;
    }
}
