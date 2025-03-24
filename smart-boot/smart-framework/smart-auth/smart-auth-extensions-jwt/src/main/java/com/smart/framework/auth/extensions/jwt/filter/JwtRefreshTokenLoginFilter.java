package com.smart.framework.auth.extensions.jwt.filter;

import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.auth.core.matcher.ExtensionPathMatcher;
import com.smart.framework.auth.core.utils.TokenUtils;
import com.smart.framework.auth.extensions.jwt.token.JwtTokenRepository;
import com.smart.framework.commons.core.i18n.I18nUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * jwt使用refresh token重新登录
 * @author shizhongming
 * 2025/3/23 19:48
 * @since 5.0.0
 */
public class JwtRefreshTokenLoginFilter extends OncePerRequestFilter {

    private final ExtensionPathMatcher requestMatcher;

    private JwtTokenRepository jwtTokenRepository;

    public JwtRefreshTokenLoginFilter(String refreshTokenLoginUrl) {
        this.requestMatcher = new ExtensionPathMatcher(refreshTokenLoginUrl);
    }

    public JwtRefreshTokenLoginFilter(ExtensionPathMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String refreshToken = TokenUtils.getRefreshToken(request);
        if (!StringUtils.hasText(refreshToken)) {
            throw new AuthenticationServiceException(I18nUtils.get(AuthI18nMessage.ERROR_TOKEN_EMPTY));
        }
    }


    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return this.requestMatcher.matches(request);
    }

    @Autowired
    public void setJwtTokenRepository(JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenRepository = jwtTokenRepository;
    }
}
