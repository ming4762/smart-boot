package com.smart.framework.auth.core.handler;

import com.smart.framework.auth.common.constants.LoginTypeEnum;
import com.smart.framework.auth.core.model.LoginResult;
import com.smart.framework.auth.core.model.RestUserDetailsImpl;
import com.smart.framework.commons.core.dto.auth.Permission;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author ShiZhongMing
 * 2021/3/1 9:58
 * @since 1.0
 */
public class DefaultAuthSuccessDataHandler implements AuthSuccessDataHandler {
    @Override
    public LoginResult successData(Authentication authentication, HttpServletRequest request, LoginTypeEnum loginType) {
        final RestUserDetailsImpl userDetails = (RestUserDetailsImpl) authentication.getPrincipal();
        userDetails.setPassword(null);
        // 处理用户权限信息
        return LoginResult.builder()
                .user(userDetails)
                .token(userDetails.getToken())
                .roles(userDetails.getRoles())
                .permissions(
                        Optional.of(userDetails.getPermissions())
                                .map(item -> item.stream().map(Permission::getAuthority).collect(Collectors.toSet()))
                                .orElse(Set.of())
                ).build();
    }
}
