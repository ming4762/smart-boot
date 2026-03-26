package com.smart.cloud.starter.auth.repository;

import com.smart.framework.auth.common.authentication.SmartAuthRestUserDetailAuthentication;
import com.smart.framework.auth.common.userdetails.PermissionGrantedAuthority;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.userdetails.RestUserDetailsImpl;
import com.smart.framework.auth.common.userdetails.RoleGrantedAuthority;
import com.smart.framework.auth.core.utils.TokenUtils;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.module.api.auth.AuthApi;
import com.smart.module.api.auth.dto.AuthUserDetailsDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 远程调用获取登录用户信息并缓存 生成SecurityContext
 * @author zhongming4762
 * 2023/3/9
 */
@Slf4j
public class RemoteSecurityContextRepository implements SecurityContextRepository {

    public static final String USER_CACHE_NAME = "auth_user_cache";

    public static final String TOKEN_ACCESS_CACHE_NAME = "smart_auth_token_access";
    private static final String LAST_CLEAR_TIME_KEY = "smart_auth_last_clear_time";
    private static final String TOKEN_CACHED_KEY = "smart_auth_cached_token";
    private static final Duration CLEAR_TOKEN_INTERVAL = Duration.ofMinutes(10);
    private static final Duration TOKEN_EXPIRE_TIME = Duration.ofHours(6);

    private final CacheManager cacheManager;

    private final AuthApi authApi;

    public RemoteSecurityContextRepository(CacheManager cacheManager, AuthApi authApi) {
        this.cacheManager = cacheManager;
        this.authApi = authApi;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String token = this.getToken(request);
        if (!StringUtils.hasText(token)) {
            return this.generateNewContext();
        }
        Cache cache = this.getUserCache();
        // 清除过期的token，防止内存泄露
        this.clearCachedToken();
        if (cache.get(token) != null) {
            RestUserDetails userDetails = (RestUserDetails) Optional.ofNullable(cache.get(token)).map(Cache.ValueWrapper::get).orElse(null);
            return this.generateSecurityContext(userDetails);
        }
        AuthUserDetailsDTO dto;
        try {
            dto = this.authApi.getUserDetails(token);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            throw new SystemException("获取用户信息失败:" + e.getMessage(), e);
        }
        if (dto == null || dto.getUserId() == null) {
            return this.generateNewContext();
        }
        RestUserDetailsImpl restUserDetails = RestUserDetailsImpl.builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .fullName(dto.getFullName())
                .locale(dto.getLocale())
                .loginTime(dto.getLoginTime())
                .roles(
                        dto.getUserAccountData().getRoleCodes().stream()
                                .map(RoleGrantedAuthority::new).collect(Collectors.toSet())
                ).permissions(
                        dto.getUserAccountData().getPermissions()
                                .stream()
                                .map(PermissionGrantedAuthority::new).collect(Collectors.toSet())
                )
                .loginIp(dto.getLoginIp())
                .bindIp(dto.getBindIp())
                .ipWhiteList(dto.getIpWhiteList())
                .token(token)
                .userTenant(dto.getUserAccountData().getTenant())
                .build();

        cache.put(token, restUserDetails);
        this.setCachedToken(token);

        return this.generateSecurityContext(restUserDetails);
    }

    private void setCachedToken(String token) {
        Cache cache = this.getTokenAccessCache();
        if (cache.get(TOKEN_CACHED_KEY) == null) {
            cache.put(TOKEN_CACHED_KEY, new ConcurrentHashMap<String, Instant>(10));
        }
        ((Map<String, Instant>)cache.get(TOKEN_CACHED_KEY).get()).put(token, Instant.now());
    }

    private void clearCachedToken() {
        Cache cache = this.getTokenAccessCache();
        Cache.ValueWrapper lastClearTime = cache.get(LAST_CLEAR_TIME_KEY);
        if (lastClearTime == null) {
            cache.put(LAST_CLEAR_TIME_KEY, Instant.now());
            return;
        }
        if (Instant.now().isAfter(((Instant) Objects.requireNonNull(lastClearTime.get())).plus(CLEAR_TOKEN_INTERVAL))) {
            Map<String, Instant> cachedToken = Optional.ofNullable(cache.get(TOKEN_CACHED_KEY))
                    .map(item -> (Map<String, Instant>) item.get())
                    .orElse(null);
            if (cachedToken == null || CollectionUtils.isEmpty(cachedToken)) {
                return;
            }

            Cache userCache = this.getUserCache();
            cachedToken.forEach((key, value) -> {
                if (Instant.now().isAfter(value.plus(TOKEN_EXPIRE_TIME))) {
                    cachedToken.remove(key);
                    userCache.evict(key);
                }
            });
        }
    }

    private Cache getTokenAccessCache() {
        return Objects.requireNonNull(this.cacheManager.getCache(TOKEN_ACCESS_CACHE_NAME));
    }

    private Cache getUserCache() {
        return Objects.requireNonNull(this.cacheManager.getCache(USER_CACHE_NAME));
    }

    protected SecurityContext generateSecurityContext(RestUserDetails user) {
        SmartAuthRestUserDetailAuthentication authentication = new SmartAuthRestUserDetailAuthentication(user);
        SecurityContext securityContext = generateNewContext();
        securityContext.setAuthentication(authentication);
        return securityContext;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        // do nothing
    }

    /**
     * Allows the repository to be queried as to whether it contains a security context
     * for the current request.
     *
     * @param request the current request
     * @return true if a context is found for the request, false otherwise
     */
    @Override
    public boolean containsContext(HttpServletRequest request) {
        return StringUtils.hasText(this.getToken(request));
    }

    protected String getToken(HttpServletRequest request) {
        return TokenUtils.getToken(request);
    }

    protected SecurityContext generateNewContext() {
        return SecurityContextHolder.createEmptyContext();
    }

}
