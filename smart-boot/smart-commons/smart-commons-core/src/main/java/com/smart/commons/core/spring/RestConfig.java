package com.smart.commons.core.spring;

import com.smart.commons.core.utils.RestUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * @author ShiZhongMing
 * 2022/2/8
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
public class RestConfig {

    private final RestTemplate restTemplate;

    public RestConfig(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        RestUtils.setRestTemplate(this.restTemplate);
    }
}
