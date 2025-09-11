package com.smart.boot.autoconfigure.ai.dify;

import com.smart.framework.ai.dify.api.DefaultDifyClient;
import com.smart.framework.ai.dify.api.DefaultDifyDatasetClient;
import com.smart.framework.ai.dify.api.DifyClient;
import com.smart.framework.ai.dify.api.DifyDatasetClient;
import com.smart.framework.ai.dify.config.ClientConfig;
import com.smart.framework.commons.core.spring.EnableRest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * dify 自动配置
 * @author shizhongming
 * 2025/2/8 21:20
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DifyClient.class)
@EnableConfigurationProperties(SmartAiDifyProperties.class)
@EnableRest
public class SmartAiDifyAutoConfiguration {

    /**
     * dify 客户端
     * @param properties 配置参数
     * @return DifyClient
     */
    @ConditionalOnMissingBean(DifyClient.class)
    @Bean
    public DifyClient difyClient(SmartAiDifyProperties properties) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setApiUrl(properties.getApiUrl());
        clientConfig.setApiKey(properties.getApiKey());

        return new DefaultDifyClient(clientConfig);
    }

    /**
     * dify 知识库客户端
     * @param properties 配置参数
     * @return DifyDatasetClient
     */
    @ConditionalOnMissingBean(DifyDatasetClient.class)
    @Bean
    public DifyDatasetClient difyDatasetClient(SmartAiDifyProperties properties) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setApiUrl(properties.getApiUrl());
        clientConfig.setApiKey(properties.getApiKey());

        return new DefaultDifyDatasetClient(clientConfig);
    }
}
