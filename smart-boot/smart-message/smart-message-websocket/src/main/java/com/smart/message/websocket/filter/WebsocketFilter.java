package com.smart.message.websocket.filter;

import com.google.common.net.HttpHeaders;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author shizhongming
 * 2023/1/22 16:50
 * @since 3.0.0
 */
public class WebsocketFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String protocol = request.getHeader(HttpHeaders.SEC_WEBSOCKET_PROTOCOL);
        if (StringUtils.hasText(protocol)) {
            response.setHeader(HttpHeaders.SEC_WEBSOCKET_PROTOCOL, protocol);
        }
        chain.doFilter(request, response);
    }
}
