package com.smart.auth.extensions.jwt.filter;

import com.smart.auth.core.exception.IpBindAuthenticationException;
import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.utils.AuthCheckUtils;
import com.smart.auth.extensions.jwt.context.JwtContext;
import com.smart.auth.extensions.jwt.store.JwtStore;
import com.smart.auth.extensions.jwt.utils.JwtUtils;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * jwt拦截器
 * @author shizhongming
 * 2020/2/15 10:58 上午
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final List<JwtStore> jwtStoreList;

    private final JwtContext jwtContext;

    public JwtAuthenticationFilter(JwtContext jwtContext, List<JwtStore> jwtStoreList) {
        this.jwtContext = jwtContext;
        this.jwtStoreList = jwtStoreList;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof RestUserDetails)) {
            throw new BadCredentialsException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EMPTY));
        }
        RestUserDetails user = (RestUserDetails) principal;
        // 验证JWT是否有效
        String jwt = JwtUtils.getJwt(request);
        if (StringUtils.isBlank(jwt)) {
            throw new AuthenticationServiceException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EMPTY));
        }
        Assert.notNull(jwt, "系统发生未知错误，jwt为null");
        // 验证JWT
        boolean validate = this.jwtStoreList.stream().anyMatch(item -> item.validate(jwt, user));
        if (!validate) {
            throw new CredentialsExpiredException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EXPIRE));
        }
        // 验证IP
        if (Boolean.TRUE.equals(user.getBindIp()) && !StringUtils.equals(user.getLoginIp(), IpUtils.getIpAddr(request))) {
            throw new IpBindAuthenticationException(I18nUtils.get(AuthI18nMessage.ERROR_IP_VALIDATE));
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return Objects.equals(this.jwtContext.getAuthProperties().getDevelopment(), Boolean.TRUE) || AuthCheckUtils.checkIgnores(request, this.jwtContext.getAuthProperties().getIgnores());
    }

}
