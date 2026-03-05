package com.smart.boot.autoconfigure.common;

import com.smart.framework.commons.core.timezone.SmartTimezoneFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 时区自动配置类
 * 自动注入时区
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-05 11:18
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SmartTimezoneAutoconfiguration {

    @Bean
    public SmartTimezoneFilter smartTimezoneFilter() {
        return new SmartTimezoneFilter();
    }
}
