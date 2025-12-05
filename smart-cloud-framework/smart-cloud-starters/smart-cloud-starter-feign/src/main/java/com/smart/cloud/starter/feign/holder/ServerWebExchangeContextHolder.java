package com.smart.cloud.starter.feign.holder;

import org.springframework.web.server.ServerWebExchange;

/**
 * ServerWebExchange持有者
 * @author shizhongming
 * 2025/10/11 10:27
 * @since 5.0.0
 */
public class ServerWebExchangeContextHolder {

    private static final ThreadLocal<ServerWebExchange> HOLDER = new ThreadLocal<>();

    public static void set(ServerWebExchange exchange) {
        HOLDER.set(exchange);
    }

    public static ServerWebExchange getExchange() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
