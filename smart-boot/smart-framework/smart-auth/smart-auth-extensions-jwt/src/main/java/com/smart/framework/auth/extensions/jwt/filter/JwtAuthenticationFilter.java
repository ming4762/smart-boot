package com.smart.framework.auth.extensions.jwt.filter;

import com.smart.framework.auth.core.exception.IpBindAuthenticationException;
import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.auth.core.properties.AuthIgnoreProperties;
import com.smart.framework.auth.core.token.TokenRepository;
import com.smart.framework.auth.core.userdetails.RestUserDetails;
import com.smart.framework.auth.core.utils.AuthCheckUtils;
import com.smart.framework.auth.core.utils.TokenUtils;
import com.smart.framework.commons.core.i18n.I18nUtils;
import com.smart.framework.commons.core.utils.IpUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * jwt拦截器
 * @author shizhongming
 * 2020/2/15 10:58 上午
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private List<TokenRepository> tokenRepositoryList;

    private final boolean development;
    private final AuthIgnoreProperties authIgnoreProperties;

    public JwtAuthenticationFilter(Boolean development, AuthIgnoreProperties authIgnoreProperties) {
        this.development = Boolean.TRUE.equals(development);
        this.authIgnoreProperties = authIgnoreProperties;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 验证JWT是否有效
        String jwt = TokenUtils.getToken(request);
        if (StringUtils.isBlank(jwt)) {
            throw new AuthenticationServiceException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EMPTY));
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof RestUserDetails user)) {
            throw new CredentialsExpiredException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EXPIRE));
        }
        // 验证JWT
        boolean validate = this.tokenRepositoryList.stream().anyMatch(item -> item.validate(jwt, user));
        if (!validate) {
            throw new CredentialsExpiredException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EXPIRE));
        }
        // 验证IP
        if (Boolean.TRUE.equals(user.getBindIp()) && !org.apache.commons.codec.binary.StringUtils.equals(user.getLoginIp(), IpUtils.getIpAddr(request))) {
            throw new IpBindAuthenticationException(I18nUtils.get(AuthI18nMessage.ERROR_IP_VALIDATE));
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return this.development || AuthCheckUtils.checkIgnores(request, this.authIgnoreProperties);
    }

    @Autowired
    public void setJwtStoreList(List<TokenRepository> tokenRepositoryList) {
        this.tokenRepositoryList = tokenRepositoryList;
    }
}
