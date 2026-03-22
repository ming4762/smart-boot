package com.smart.framework.commons.core.timezone;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZoneId;

/**
 * 从请求头获取时区并添加到时区上下文
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-05 10:23
 * @since 5.0.0
 */
public class SmartTimezoneFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 从请求头获取时区
        ZoneId userZoneId = SmartTimezoneContext.getFromHeader(request);
        if (userZoneId == null) {
            // 如果请求头中没有时区信息，则使用默认时区
            userZoneId = ZoneId.systemDefault();
        }
        SmartTimezoneContext.run(userZoneId, () -> {
            try {
                filterChain.doFilter(request, response);
            } catch (IOException | ServletException exception) {
                throw new RuntimeException(exception);
            }
        });
    }
}
