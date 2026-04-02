package com.smart.boot.n8n;

import com.smart.framework.n8n.properties.SmartN8nProperties;
import com.smart.framework.n8n.service.DefaultSmartN8nServiceImpl;
import com.smart.framework.n8n.service.SmartN8nService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * N8n自动配置类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-07 20:53
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SmartN8nService.class)
@EnableConfigurationProperties(SmartN8nProperties.class)
public class SmartN8nAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SmartN8nService.class)
    public SmartN8nService smartN8nService(SmartN8nProperties properties) {
        return new DefaultSmartN8nServiceImpl(properties);
    }
}
