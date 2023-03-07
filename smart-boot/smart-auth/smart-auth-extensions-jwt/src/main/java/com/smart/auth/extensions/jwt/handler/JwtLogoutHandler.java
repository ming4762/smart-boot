package com.smart.auth.extensions.jwt.handler;

import com.smart.auth.core.exception.AuthException;
import com.smart.auth.core.handler.SecurityLogoutHandler;
import com.smart.auth.core.token.TokenRepository;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.auth.extensions.jwt.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

/**
 * @author ShiZhongMing
 * 2022/8/9
 * @since 3.0.0
 */
public class JwtLogoutHandler implements SecurityLogoutHandler {

    private final TokenRepository tokenRepository;

    public JwtLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String jwt = JwtUtils.getJwt(request);
        if (StringUtils.isBlank(jwt)) {
            throw new AuthException("JWT为null，无法登出");
        }
        this.tokenRepository.invalidateByToken(AuthUtils.getNonNullCurrentUser().getUsername(), jwt);
    }
}
