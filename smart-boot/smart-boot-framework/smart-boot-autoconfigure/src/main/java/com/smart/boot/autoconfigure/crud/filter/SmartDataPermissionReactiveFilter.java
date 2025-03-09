package com.smart.boot.autoconfigure.crud.filter;

import com.smart.framework.crud.datapermission.handler.SmartDataContextHolder;
import com.smart.framework.crud.datapermission.handler.SmartDataPermissionController;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 数据权限过滤器
 * 清空上下文缓存
 * @author shizhongming
 * 2025/3/7 14:53
 * @since 5.0.0
 */
public class SmartDataPermissionReactiveFilter implements WebFilter {
    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).doFinally(signalType  -> {
            SmartDataPermissionController.clear();
            SmartDataContextHolder.clear();
        });
    }
}
