package com.smart.auth.security.filter;

import com.smart.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import com.smart.auth.core.handler.AuthSuccessDataHandler;
import com.smart.auth.core.model.LoginResult;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.userdetails.UserDetailsBuilder;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.dto.auth.UserTenantDTO;
import com.smart.commons.core.exception.SystemException;
import com.smart.commons.core.http.HttpMethod;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.tenant.SmartTenantHolder;
import com.smart.commons.core.utils.RestJsonWriter;
import com.smart.module.api.auth.AuthApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Tenant 拦截器
 * @author shizhongming
 * 2024/4/9 11:10
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class SmartAuthTenantChangeFilter extends OncePerRequestFilter {

    private static final String CHANGE_TENANT_URL = "/auth/tenant/change";

    private static final String TENANT_ID_KEY = "tenantId";

    private final UserDetailsBuilder userDetailsBuilder;
    private final AuthApi authApi;
    private final SecurityContextRepository securityContextRepository;
    private final AuthSuccessDataHandler authSuccessDataHandler;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 切换租户
        this.changeTenant(request, response);
    }

    /**
     * 切换租户
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @SneakyThrows(IOException.class)
    private void changeTenant(HttpServletRequest request, HttpServletResponse response) {
        String tenantStr = request.getParameter(TENANT_ID_KEY);
        if (!StringUtils.hasText(tenantStr)) {
            throw new SystemException("切换租户失败，租户ID不存在");
        }
        RestUserDetails currentUser = AuthUtils.getCurrentUser();
        if (currentUser == null) {
            throw new SystemException("用户未登录，无法切换租户");
        }
        Long tenantId = Long.valueOf(tenantStr);
        UserTenantDTO tenant = new UserTenantDTO();
        tenant.setTenantId(tenantId);
        SmartTenantHolder.set(tenant);

        AuthUserDTO authUser = AuthUserDTO.builder()
                .userId(currentUser.getUserId())
                .username(currentUser.getUsername())
                .fullName(currentUser.getFullName())
                .password(currentUser.getPassword())
                .build();
        RestUserDetails newRestUser = this.userDetailsBuilder.buildUserDetails(authUser);
        // 移除原有token
        this.authApi.offlineByToken(currentUser.getToken());
        // 保存新token
        RestUsernamePasswordAuthenticationToken authentication = new RestUsernamePasswordAuthenticationToken(newRestUser, null, newRestUser.getAuthorities(), newRestUser.getBindIp(), newRestUser.getLoginIp(), newRestUser.getLoginType());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        this.securityContextRepository.saveContext(securityContext, request, response);
        // 返回新的登录数据
        LoginResult loginResult = this.authSuccessDataHandler.successData(authentication, request, newRestUser.getLoginType());
        RestJsonWriter.writeJson(response, Result.success(loginResult));
    }

    /**
     * 是否是切换租户请求
     * @param request HttpServletRequest
     * @return 是否是切换租户请求
     */
    private boolean isChangeTenant(@NonNull HttpServletRequest request) {
        return new AntPathRequestMatcher(CHANGE_TENANT_URL, HttpMethod.POST.name()).matches(request);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !this.isChangeTenant(request);
    }
}
