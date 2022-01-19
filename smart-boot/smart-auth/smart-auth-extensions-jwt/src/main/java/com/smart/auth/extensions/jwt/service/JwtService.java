package com.smart.auth.extensions.jwt.service;

import com.smart.auth.core.exception.AuthException;
import com.smart.auth.core.handler.SecurityLogoutHandler;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.auth.extensions.jwt.resolver.JwtResolver;
import com.smart.auth.extensions.jwt.store.CacheJwtStore;
import com.smart.auth.extensions.jwt.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT服务层
 * 1、登出逻辑
 * 2、jwt解析接口
 * 3、jwtStore接口 cache实现
 * @author ShiZhongMing
 * 2020/12/31 15:43
 * @since 1.0
 */
@Slf4j
public class JwtService implements SecurityLogoutHandler, JwtResolver {


    private final AuthProperties authProperties;

    private final CacheJwtStore cacheJwtStore;

    public JwtService(AuthProperties authProperties, CacheJwtStore cacheJwtStore) {
        this.authProperties = authProperties;
        this.cacheJwtStore = cacheJwtStore;
    }

    @Override
    public RestUserDetails resolver(@NonNull String jwt) {
        return JwtUtils.getUser(jwt, this.authProperties.getJwtKey());
    }

    @Override
    public String create(RestUserDetails userDetails) {
        return JwtUtils.createJwt(userDetails, this.authProperties.getJwtKey());
    }

    /**
     * 执行登出操作
     * @param request request
     * @param response response
     * @param authentication authentication
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String jwt = JwtUtils.getJwt(request);
        if (StringUtils.isBlank(jwt)) {
            throw new AuthException("JWT为null，无法登出");
        }
        this.cacheJwtStore.invalidateByToken(AuthUtils.getNonNullCurrentUser().getUsername(), jwt);
    }


    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

}
