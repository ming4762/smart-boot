package com.smart.boot.autoconfigure.common;

import com.smart.framework.commons.core.event.SmartEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * smart-boot 通用事件自动配置
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-15 17:57
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SmartEventAutoConfiguration {

    @Bean
    public SmartEventListener smartEventListener() {
        return new SmartEventListener();
    }
}
