package com.smart.auth.extensions.jwt.service;

import com.smart.auth.core.constants.LoginTypeEnum;
import com.smart.auth.core.exception.AuthException;
import com.smart.auth.core.handler.SecurityLogoutHandler;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.extensions.jwt.resolver.JwtResolver;
import com.smart.auth.extensions.jwt.store.JwtStore;
import com.smart.auth.extensions.jwt.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Objects;

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
public class JwtService implements SecurityLogoutHandler, JwtResolver, JwtStore {

    private static final String TOKE_KEY_PREFIX = "user";

    private static final String DATA_KEY_PREFIX = "attribute";

    private final AuthCache<String, Object> authCache;

    private final AuthProperties authProperties;

    public JwtService(AuthProperties authProperties, AuthCache<String, Object> authCache) {
        this.authCache = authCache;
        this.authProperties = authProperties;
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
        this.authCache.put(this.getTokenKey(user.getUsername(), jwt), timeout, timeout);
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
        Duration timeout = (Duration) this.authCache.get(jwtKey);
        if (ObjectUtils.isNotEmpty(timeout)) {
            this.authCache.expire(jwtKey, timeout);
            this.authCache.expire(attributeKey, timeout);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public RestUserDetails resolver(@NonNull String jwt, HttpServletRequest request) {
        return JwtUtils.getUser(jwt, this.authProperties.getJwtKey());
    }

    @Override
    public String create(RestUserDetails userDetails) {
        return JwtUtils.createJwt(userDetails, this.authProperties.getJwtKey());
    }

    /**
     * 获取token的key
     * @param username 用户名
     * @return jst
     */
    @NonNull
    protected String getTokenKey(@NonNull String username, @NonNull String jwt) {
        return String.join(":", this.authProperties.getPrefix(), TOKE_KEY_PREFIX, username, jwt);
    }

    protected String getAttributeKey(@NonNull String username,  @NonNull String jwt) {
        return String.join(":", this.authProperties.getPrefix(), DATA_KEY_PREFIX, username, jwt);
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
        final RestUserDetails user = JwtUtils.getUser(jwt, this.authProperties.getJwtKey());
        if (Objects.nonNull(this.authCache.get(this.getTokenKey(user.getUsername(), jwt)))) {
            Assert.notNull(user, "系统发生未知异常：未找到当前用户");
            Assert.notNull(jwt, "系统发生未知异常，未找到token");
            this.authCache.remove(this.getTokenKey(user.getUsername(), jwt));
            this.authCache.remove(this.getAttributeKey(user.getUsername(), jwt));
        } else {
            log.debug("用户已登出");
        }

    }


    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

}
