package com.smart.boot.crud.filter;

import com.smart.framework.crud.plus.tenant.SmartTenantControl;
import org.jspecify.annotations.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author shizhongming
 * 2025/2/13 14:13
 * @since 5.0.0
 */
public class SmartTenantReactiveFilter implements WebFilter {

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).doFinally(signalType  -> SmartTenantControl.clear());
    }
}
