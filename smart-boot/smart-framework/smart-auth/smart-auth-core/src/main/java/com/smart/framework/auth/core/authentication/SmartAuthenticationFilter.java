package com.smart.framework.auth.core.authentication;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.auth.core.exception.IpBindAuthenticationException;
import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.commons.core.i18n.I18nUtils;
import com.smart.framework.commons.core.utils.IpUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 登录验证拦截器
 * @author shizhongming
 * 2025/3/13 16:21
 * @since 5.0.0
 */
public class SmartAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        Object principal = AuthUtils.getAuthentication().getPrincipal();
        if (!(principal instanceof RestUserDetails user)) {
            throw new CredentialsExpiredException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EXPIRE));
        }
        // 验证IP
        if (Boolean.TRUE.equals(user.getBindIp()) && !org.apache.commons.codec.binary.StringUtils.equals(user.getLoginIp(), IpUtils.getIpAddr(request))) {
            throw new IpBindAuthenticationException(I18nUtils.get(AuthI18nMessage.ERROR_IP_VALIDATE));
        }
        filterChain.doFilter(request, response);
    }
}
