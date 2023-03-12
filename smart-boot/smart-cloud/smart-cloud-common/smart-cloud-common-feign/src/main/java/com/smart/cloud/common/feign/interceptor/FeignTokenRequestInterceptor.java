package com.smart.cloud.common.feign.interceptor;

import com.smart.cloud.common.feign.utils.TokenHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * @author zhongming4762
 * 2023/3/10
 */
@Configuration
public class FeignTokenRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String token = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .map(item -> item.getHeader(HttpHeaders.AUTHORIZATION))
                .orElse(null);
        if (token == null) {
            token = TokenHolder.get();
        }
        if (StringUtils.hasText(token)) {
            template.header(HttpHeaders.AUTHORIZATION, token);
        }
    }
}
