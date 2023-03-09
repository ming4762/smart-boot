package com.smart.cloud.common.auth.repository;

import com.google.common.collect.Sets;
import com.smart.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import com.smart.auth.core.model.PermissionGrantedAuthority;
import com.smart.auth.core.model.RestUserDetailsImpl;
import com.smart.auth.core.model.RoleGrantedAuthority;
import com.smart.auth.core.model.SmartGrantedAuthority;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.module.api.auth.AuthApi;
import com.smart.module.api.auth.dto.AuthUserDetailsDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;

/**
 * 远程调用获取登录用户信息并缓存 生成SecurityContext
 * @author zhongming4762
 * 2023/3/9
 */
public class RemoteSecurityContextRepository implements SecurityContextRepository {

    public static final String USER_CACHE_NAME = "auth_user_cache";

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
        Cache cache = this.cacheManager.getCache(USER_CACHE_NAME);
        if (cache != null && cache.get(token) != null) {
            RestUserDetails userDetails = (RestUserDetails) Optional.ofNullable(cache.get(token)).map(Cache.ValueWrapper::get).orElse(null);
            return this.generateSecurityContext(request, userDetails);
        }
        AuthUserDetailsDTO dto = this.authApi.getUserDetails(token);
        if (dto == null) {
            return this.generateNewContext();
        }
        RestUserDetailsImpl restUserDetails = new RestUserDetailsImpl();
        restUserDetails.setUserId(dto.getUserId());
        restUserDetails.setUsername(dto.getUsername());
        restUserDetails.setFullName(dto.getFullName());
        restUserDetails.setLocale(dto.getLocale());
        restUserDetails.setLoginTime(dto.getLoginTime());
        Set<SmartGrantedAuthority> grantedAuthoritySet = Sets.newHashSet();
        grantedAuthoritySet.addAll(
                dto.getRolePermission().getRoleCodes().stream()
                        .map(RoleGrantedAuthority::new).toList()
        );
        // 添加权限
        grantedAuthoritySet.addAll(
                dto.getRolePermission().getPermissions()
                        .stream()
                        .map(PermissionGrantedAuthority::new).toList()
        );
        restUserDetails.setAuthorities(grantedAuthoritySet);
        restUserDetails.setLoginIp(dto.getLoginIp());
        restUserDetails.setBindIp(dto.getBindIp());
        restUserDetails.setIpWhiteList(dto.getIpWhiteList());
        restUserDetails.setToken(token);
        if (cache != null) {
            cache.put(token, restUserDetails);
        }
        return this.generateSecurityContext(request, restUserDetails);
    }

    protected SecurityContext generateSecurityContext(HttpServletRequest request, RestUserDetails user) {
        RestUsernamePasswordAuthenticationToken authentication = new RestUsernamePasswordAuthenticationToken(user, null, user.getAuthorities(), user.getBindIp(), user.getLoginIp(), user.getLoginType());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
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
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    protected SecurityContext generateNewContext() {
        return SecurityContextHolder.createEmptyContext();
    }
}
