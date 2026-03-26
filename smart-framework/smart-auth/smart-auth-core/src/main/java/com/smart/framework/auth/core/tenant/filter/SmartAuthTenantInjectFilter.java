package com.smart.framework.auth.core.tenant.filter;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.tenant.SmartTenantHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

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
        SmartTenantHolder.set(
                () -> Optional.ofNullable(AuthUtils.getCurrentUser())
                        .map(RestUserDetails::getUserTenant)
                        .orElse(null)
        );
        try {
            filterChain.doFilter(request, response);
        } finally {
            SmartTenantHolder.clear();
        }
    }
}
