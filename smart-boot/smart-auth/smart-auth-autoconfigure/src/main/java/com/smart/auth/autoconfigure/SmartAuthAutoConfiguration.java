package com.smart.auth.autoconfigure;

import com.smart.auth.core.event.AuthEventListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0.7
 */
@Configuration(proxyBeanMethods = false)
public class SmartAuthAutoConfiguration {

    /**
     * 创建认证事件监听器
     * @return 认证事件监听器
     */
    @Bean
    @ConditionalOnMissingBean(AuthEventListener.class)
    public AuthEventListener authEventListener() {
        return new AuthEventListener();
    }

}
