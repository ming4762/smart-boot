package com.smart.cloud.common.feign.interceptor;

import com.smart.cloud.common.feign.utils.TokenHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

/**
 * @author zhongming4762
 * 2023/3/10
 */
@Configuration
public class FeignTokenRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String token = TokenHolder.get();
        if (StringUtils.hasText(token)) {
            template.header(HttpHeaders.AUTHORIZATION, token);
        }
    }
}
