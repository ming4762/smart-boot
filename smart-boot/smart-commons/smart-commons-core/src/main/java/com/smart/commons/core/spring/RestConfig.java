package com.smart.commons.core.spring;

import com.smart.commons.core.utils.RestUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @author ShiZhongMing
 * 2022/2/8
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
public class RestConfig {

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.setConnectTimeout(Duration.ofMillis(5000L))
                .setReadTimeout(Duration.ofMillis(50000L))
                .build();
        RestUtils.setRestTemplate(restTemplate);
        return restTemplate;
    }
}
