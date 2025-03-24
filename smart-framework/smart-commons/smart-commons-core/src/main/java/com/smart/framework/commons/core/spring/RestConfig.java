package com.smart.framework.commons.core.spring;

import com.smart.framework.commons.core.utils.RestUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author ShiZhongMing
 * 2022/2/8
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
public class RestConfig {

    /**
     * 构建 WebClient
     * @param builderProvider WebClient.Builder
     * @return WebClient
     */
    @Bean
    @ConditionalOnMissingBean(WebClient.class)
    public WebClient webClient(ObjectProvider<WebClient.Builder> builderProvider) {
        WebClient.Builder builder = builderProvider.getIfAvailable(WebClient::builder);
        WebClient webClient = builder.build();
        RestUtils.setWebClient(webClient);
        return webClient;
    }
}
