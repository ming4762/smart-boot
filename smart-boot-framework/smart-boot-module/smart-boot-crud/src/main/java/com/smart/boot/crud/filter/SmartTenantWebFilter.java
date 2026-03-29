package com.smart.boot.crud.filter;

import com.smart.framework.crud.plus.tenant.SmartTenantControl;
import jakarta.servlet.*;

import java.io.IOException;

/**
 * @author shizhongming
 * 2025/2/13 14:10
 * @since 5.0.0
 */
public class SmartTenantWebFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            SmartTenantControl.clear();
        }
    }
}
