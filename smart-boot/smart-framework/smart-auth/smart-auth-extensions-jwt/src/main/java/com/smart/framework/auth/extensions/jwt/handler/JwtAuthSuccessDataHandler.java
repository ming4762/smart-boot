package com.smart.framework.auth.extensions.jwt.handler;

import com.smart.framework.auth.core.handler.AuthSuccessDataHandler;
import com.smart.framework.auth.core.model.LoginResult;
import com.smart.framework.auth.core.model.RestUserDetailsImpl;
import com.smart.framework.auth.extensions.jwt.token.JwtTokenRepository;
import com.smart.framework.commons.core.dto.auth.Permission;
import com.smart.framework.commons.core.exception.SystemException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JWT认证成功数据处理器
 * @author shizhongming
 * 2025/3/23 19:34
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class JwtAuthSuccessDataHandler implements AuthSuccessDataHandler {

    private final List<JwtTokenRepository> jwtTokenRepositoryList;

    /**
     * 登录成功数据
     *
     * @param authentication 认证信息
     * @param request        请求体
     * @param response       响应体
     * @return 登录成功数据
     */
    @Override
    public LoginResult successData(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        final RestUserDetailsImpl userDetails = (RestUserDetailsImpl) authentication.getPrincipal();
        String jwt = "";
        for (JwtTokenRepository jwtTokenRepository : this.jwtTokenRepositoryList) {
            jwt = jwtTokenRepository.generateToken(userDetails);
            if (StringUtils.hasText(jwt)) {
                break;
            }
        }
        if (!StringUtils.hasText(jwt)) {
            throw new SystemException("JWT生成失败");
        }
        userDetails.setToken(jwt);
        // 生成refresh token
        String refreshToken = "";
        for (JwtTokenRepository jwtTokenRepository : this.jwtTokenRepositoryList) {
            refreshToken = jwtTokenRepository.generateRefreshToken(userDetails);
            if (StringUtils.hasText(refreshToken)) {
                break;
            }
        }
        if (!StringUtils.hasText(refreshToken)) {
            throw new SystemException("生成refresh token失败");
        }
        // 将token放入响应头
//        TokenUtils.setHeaderToken(jwt, response);
//        TokenUtils.setHeaderRefreshToken(refreshToken, response);
        return LoginResult.builder()
                .user(userDetails)
                .token(userDetails.getToken())
                .refreshToken(refreshToken)
                .roles(userDetails.getRoles())
                .permissions(
                        Optional.of(userDetails.getPermissions())
                                .map(item -> item.stream().map(Permission::getAuthority).collect(Collectors.toSet()))
                                .orElse(Set.of())
                ).build();
    }
}
