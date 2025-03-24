package com.smart.framework.auth.extensions.jwt.handler;

import com.smart.framework.auth.common.exception.AuthException;
import com.smart.framework.auth.core.handler.SecurityLogoutHandler;
import com.smart.framework.auth.core.utils.TokenUtils;
import com.smart.framework.auth.extensions.jwt.token.JwtTokenRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/8/9
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class JwtLogoutHandler implements SecurityLogoutHandler {

    private final List<JwtTokenRepository> jwtTokenRepositoryList;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String refreshToken = TokenUtils.getRefreshToken(request);
        if (StringUtils.isBlank(refreshToken)) {
            throw new AuthException("refreshToken为null，无法登出");
        }
        for (JwtTokenRepository jwtTokenRepository : this.jwtTokenRepositoryList) {
            if (jwtTokenRepository.invalidateByToken(refreshToken)) {
                break;
            }
        }
    }
}
