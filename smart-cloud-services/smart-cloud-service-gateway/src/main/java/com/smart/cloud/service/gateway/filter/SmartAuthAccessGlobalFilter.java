package com.smart.cloud.service.gateway.filter;

import com.smart.cloud.api.auth.feign.RemoteAuthApi;
import com.smart.cloud.starter.feign.holder.ServerWebExchangeContextHolder;
import com.smart.framework.commons.core.http.HttpStatus;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.module.api.auth.dto.AuthenticationDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 用户认证拦截器
 * 验证用户是否已经登录
 * @author zhongming4762
 * 2023/3/10
 */
@Component
@Slf4j
public class SmartAuthAccessGlobalFilter implements GlobalFilter, Ordered {

    private final RemoteAuthApi remoteAuthApi;

    public SmartAuthAccessGlobalFilter(@Lazy RemoteAuthApi remoteAuthApi) {
        this.remoteAuthApi = remoteAuthApi;
    }

    @SneakyThrows(InterruptedException.class)
    @Override
    public @NonNull Mono<Void> filter(ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 获取请求路径
        String servicePath = request.getURI().getPath();
        HttpMethod httpMethod = request.getMethod();

        CompletableFuture<Result<Boolean>> future = CompletableFuture.supplyAsync(() -> {
            try {
                ServerWebExchangeContextHolder.set(exchange);
                return this.remoteAuthApi.authenticate(
                        AuthenticationDTO.builder()
                                .url(servicePath)
                                .httpMethod(httpMethod.name())
                                .build()
                );
            } finally {
                ServerWebExchangeContextHolder.clear();
            }
        });

        Result<Boolean> result;
        try {
            result = future.get();
            if (result.getCode().equals(HttpStatus.OK.getCode())) {
                return chain.filter(exchange);
            }
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
            result = Result.failure(e);
        }
        byte[] bytes = JsonUtils.toJsonString(result).getBytes(StandardCharsets.UTF_8);
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return this.writeToResponse(response, bytes);
    }

    private Mono<Void> writeToResponse(ServerHttpResponse response, byte[] data){
        return response.writeAndFlushWith(Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(data))));
    }

    @Override
    public int getOrder() {
        return 10100;
    }
}
