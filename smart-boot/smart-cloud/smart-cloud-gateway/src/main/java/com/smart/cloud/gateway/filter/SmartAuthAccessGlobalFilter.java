package com.smart.cloud.gateway.filter;

import com.smart.cloud.api.auth.feign.RemoteAuthApi;
import com.smart.cloud.common.feign.utils.TokenHolder;
import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.module.api.auth.dto.AuthenticationDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;

import java.nio.charset.StandardCharsets;
import java.util.List;
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
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 获取请求路径
        String requestPath = request.getURI().getPath();
        // 删除前缀
        String servicePath = requestPath.substring(1).substring(requestPath.indexOf("/", 1) - 1);
        HttpMethod httpMethod = request.getMethod();
        List<String> tokenList = request.getHeaders().get(HttpHeaders.AUTHORIZATION);


        CompletableFuture<Result<Boolean>> future = CompletableFuture.supplyAsync(() -> {
            if (!CollectionUtils.isEmpty(tokenList)) {
                String token = tokenList.get(0);
                TokenHolder.set(token);
            }
            return this.remoteAuthApi.authenticate(
                    AuthenticationDTO.builder()
                            .url(servicePath)
                            .httpMethod(httpMethod.name())
                            .build()
            );
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
        return 0;
    }
}
