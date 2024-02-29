package com.smart.auth.security.api.local;

import com.smart.auth.core.exception.IpBindAuthenticationException;
import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.core.token.TokenRepository;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.utils.AuthCheckUtils;
import com.smart.auth.core.utils.TokenUtils;
import com.smart.auth.core.utils.request.MatcherHttpServletRequest;
import com.smart.commons.core.dto.auth.UserRolePermission;
import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.IpUtils;
import com.smart.module.api.auth.AuthApi;
import com.smart.module.api.auth.dto.AuthCacheDTO;
import com.smart.module.api.auth.dto.AuthUserDetailsDTO;
import com.smart.module.api.auth.dto.AuthenticationDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

/**
 * @author zhongming4762
 * 2023/3/7
 */
@Component
@Primary
public class LocalAuthApiImpl implements AuthApi {

    private final AuthProperties authProperties;

    private final List<TokenRepository> tokenRepositoryList;

    private final AuthCache<String, Object> authCache;

    public LocalAuthApiImpl(List<TokenRepository> tokenRepositoryList, AuthProperties authProperties, AuthCache<String, Object> authCache) {
        this.tokenRepositoryList = tokenRepositoryList;
        this.authProperties = authProperties;
        this.authCache = authCache;
    }

    /**
     * 通过token离线
     *
     * @param token token
     * @return 结果
     */
    @Override
    public boolean offlineByToken(@NonNull String token) {
        boolean result = false;
        for (TokenRepository repository : this.tokenRepositoryList) {
            result = repository.invalidateByToken(token);
            if (result) {
                break;
            }
        }
        return result;
    }

    /**
     * 通过用户名离线
     *
     * @param username 用户名
     * @return 结果
     */
    @Override
    public boolean offlineByUsername(@NonNull String username) {
        boolean result = false;
        for (TokenRepository repository : this.tokenRepositoryList) {
            result = repository.invalidateByUsername(username);
            if (result) {
                break;
            }
        }
        return result;
    }

    /**
     * 通过token查询用户信息
     *
     * @param token token
     * @return AuthUserDetailsDTO
     */
    @Override
    public AuthUserDetailsDTO getUserDetails(@NonNull String token) {
        RestUserDetails userDetails = null;
        for (TokenRepository tokenRepository : this.tokenRepositoryList) {
            userDetails = tokenRepository.getUser(token);
            if (userDetails != null) {
                break;
            }
        }
        if (userDetails == null) {
            return null;
        }

        UserRolePermission rolePermission = new UserRolePermission();
        rolePermission.setRoleCodes(userDetails.getRoles());
        rolePermission.setPermissions(userDetails.getPermissions());
        return AuthUserDetailsDTO.builder()
                .userId(userDetails.getUserId())
                .username(userDetails.getUsername())
                .fullName(userDetails.getFullName())
                .locale(userDetails.getLocale())
                .loginTime(userDetails.getLoginTime())
                .loginIp(userDetails.getLoginIp())
                .bindIp(userDetails.getBindIp())
                .ipWhiteList(userDetails.getIpWhiteList())
                .rolePermission(rolePermission)
                .build();
    }

    /**
     * 用户鉴权
     * @param parameter 验证的参数
     * @return 验证结果
     */
    @Override
    public Result<Boolean> authenticate(AuthenticationDTO parameter) {
        HttpServletRequest request = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
        if (request == null) {
            return Result.failure(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "未知异常", false);
        }
        MatcherHttpServletRequest matcherHttpServletRequest = new MatcherHttpServletRequest(parameter.getHttpMethod(), parameter.getUrl());
        // 如果是开发模式或者忽略的路径直接返回true
        if (Boolean.TRUE.equals(this.authProperties.getDevelopment()) || AuthCheckUtils.checkIgnores(matcherHttpServletRequest, this.authProperties.getIgnores())) {
            return Result.success(true);
        }
        try {
            boolean result = this.authenticate(request);
            return Result.success(result);
        } catch (AuthenticationException e) {
            return Result.failure(org.springframework.http.HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    protected boolean authenticate(HttpServletRequest request) {
        // 验证JWT是否有效
        String token = TokenUtils.getToken(request);
        if (StringUtils.isBlank(token)) {
            throw new AuthenticationServiceException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EMPTY));
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof RestUserDetails user)) {
            throw new BadCredentialsException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EXPIRE));
        }
        // 验证JWT
        boolean validate = this.tokenRepositoryList.stream().anyMatch(item -> item.validate(token, user));
        if (!validate) {
            throw new CredentialsExpiredException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EXPIRE));
        }
        // 验证IP
        if (Boolean.TRUE.equals(user.getBindIp()) && !StringUtils.equals(user.getLoginIp(), IpUtils.getIpAddr(request))) {
            throw new IpBindAuthenticationException(I18nUtils.get(AuthI18nMessage.ERROR_IP_VALIDATE));
        }
        return true;
    }

    /**
     * 获取认证缓存
     *
     * @param key key
     * @return 缓存对象
     */
    @Override
    public Object getAuthCache(@NonNull String key) {
        return this.authCache.get(key);
    }

    /**
     * 设置缓存信息
     *
     * @param parameter 参数
     */
    @Override
    public void setAuthCache(@NonNull AuthCacheDTO parameter) {
        this.authCache.put(parameter.getKey(), parameter.getData(), parameter.getTimeout());
    }

    /**
     * 删除缓存
     *
     * @param key 缓存的key
     */
    @Override
    public void removeAuthCache(@NonNull String key) {
        this.authCache.remove(key);
    }
}
