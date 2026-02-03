package com.smart.framework.auth.extensions.jwt.handler;

import com.smart.framework.auth.common.exception.AuthException;
import com.smart.framework.auth.core.handler.SecurityLogoutHandler;
import com.smart.framework.auth.core.utils.TokenUtils;
import com.smart.framework.auth.extensions.jwt.token.JwtTokenRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author ShiZhongMing
 * 2022/8/9
 * @since 3.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class JwtLogoutHandler implements SecurityLogoutHandler {

    private final JwtTokenRepository jwtTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = TokenUtils.getToken(request);
        if (StringUtils.isBlank(token)) {
            throw new AuthException("token为null，无法登出");
        }
        try {
            this.jwtTokenRepository.invalidateByToken(token);
        } catch (AuthenticationException e) {
            log.warn("登出失败:{}", e.getMessage(), e);
        }
    }
}
