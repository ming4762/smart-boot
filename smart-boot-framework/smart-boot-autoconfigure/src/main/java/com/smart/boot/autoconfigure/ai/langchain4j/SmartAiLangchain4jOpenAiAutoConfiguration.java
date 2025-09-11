package com.smart.boot.autoconfigure.ai.langchain4j;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * langchain4j openai 自动配置
 * @author shizhongming
 * 2025/9/11 13:38
 * @since 5.0.0
 */
@Configuration
@EnableConfigurationProperties(SmartOpenAiProperties.class)
public class SmartAiLangchain4jOpenAiAutoConfiguration {

    @Bean
    public ChatModelBeanDefinitionRegistryPostProcessor chatModelBeanDefinitionRegistryPostProcessor() {
        return new ChatModelBeanDefinitionRegistryPostProcessor();
    }
}
