package com.smart.boot.crud.filter;

import com.smart.framework.crud.datapermission.handler.SmartDataContextHolder;
import com.smart.framework.crud.datapermission.handler.SmartDataPermissionController;
import jakarta.servlet.*;

import java.io.IOException;

/**
 * 数据权限过滤器
 * 清空上下文缓存
 * @author shizhongming
 * 2025/3/7 14:50
 * @since 5.0.0
 */
public class SmartDataPermissionWebFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            this.clear();
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            this.clear();
        }
    }

    private void clear() {
        SmartDataPermissionController.clear();
        SmartDataContextHolder.clear();
    }
}
