package com.smart.auth.core.handler;

import com.google.common.collect.Sets;
import com.smart.auth.core.constants.LoginTypeEnum;
import com.smart.auth.core.model.LoginResult;
import com.smart.auth.core.model.Permission;
import com.smart.auth.core.userdetails.RestUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/3/1 9:58
 * @since 1.0
 */
public class DefaultAuthSuccessDataHandler implements AuthSuccessDataHandler {
    @Override
    public LoginResult successData(Authentication authentication, HttpServletRequest request, LoginTypeEnum loginType) {
        final RestUserDetails userDetails = (RestUserDetails) authentication.getPrincipal();
        // 处理用户权限信息
        return LoginResult.builder()
                .user(userDetails)
                .token(userDetails.getToken())
                .roles(userDetails.getRoles())
                .permissions(
                        Optional.of(userDetails.getPermissions())
                                .map(item -> item.stream().map(Permission::getAuthority).collect(Collectors.toSet()))
                                .orElse(Sets.newHashSet())
                ).build();
    }
}
