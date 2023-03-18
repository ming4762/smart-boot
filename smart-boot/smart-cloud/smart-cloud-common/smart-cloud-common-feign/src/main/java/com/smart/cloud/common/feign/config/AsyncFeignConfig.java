package com.smart.cloud.common.feign.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


/**
 * 异步调用feign配置类
 * @author zhongming4762
 * 2023/3/10
 */
@Configuration
public class AsyncFeignConfig {

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters() {
        return new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
    }
}
