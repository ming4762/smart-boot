package com.smart.starter.xxl;

import com.smart.starter.xxl.config.SmartXxlExecutorConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author shizhongming
 * 2024/6/26 20:50
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SmartXxlExecutorProperties.class)
@Import(SmartXxlExecutorConfig.class)
public class SmartXxlExecutorAutoConfiguration {
}
