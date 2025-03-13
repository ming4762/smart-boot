package com.smart.framework.auth.extensions.jwt.handler;

import com.smart.framework.auth.common.exception.AuthException;
import com.smart.framework.auth.core.handler.SecurityLogoutHandler;
import com.smart.framework.auth.core.utils.TokenUtils;
import com.smart.framework.auth.extensions.jwt.token.JwtTokenRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

/**
 * @author ShiZhongMing
 * 2022/8/9
 * @since 3.0.0
 */
public class JwtLogoutHandler implements SecurityLogoutHandler {

    private final JwtTokenRepository tokenRepository;

    public JwtLogoutHandler(JwtTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String jwt = TokenUtils.getToken(request);
        if (StringUtils.isBlank(jwt)) {
            throw new AuthException("JWT为null，无法登出");
        }
        this.tokenRepository.invalidateByToken(jwt);
    }
}
