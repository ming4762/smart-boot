package com.smart.framework.auth.core.tenant.filter;

import com.smart.framework.auth.core.userdetails.RestUserDetails;
import com.smart.framework.auth.core.utils.AuthUtils;
import com.smart.framework.commons.core.tenant.SmartTenantHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Tenant注入 拦截器
 * @author shizhongming
 * 2024/4/9 11:10
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class SmartAuthTenantInjectFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        RestUserDetails currentUser = AuthUtils.getCurrentUser();
        if (currentUser != null) {
            SmartTenantHolder.set(currentUser.getUserTenant());
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            SmartTenantHolder.clear();
        }
    }
}
