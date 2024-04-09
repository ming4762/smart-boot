package com.smart.auth.security.filter;

import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.tenant.SmartTenantHolder;
import jakarta.servlet.*;

import java.io.IOException;

/**
 * Tenant 拦截器
 * @author shizhongming
 * 2024/4/9 11:10
 * @since 3.0.0
 */
public class SmartAuthTenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RestUserDetails currentUser = AuthUtils.getCurrentUser();
        if (currentUser != null) {
            SmartTenantHolder.set(currentUser.getUserTenant().getTenantId());
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            SmartTenantHolder.clear();
        }
    }
}
