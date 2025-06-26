package com.smart.cloud.api.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2025/6/16 9:48
 * @since 5.0.0
 */
@Configuration
@ComponentScan
@EnableFeignClients(basePackages = {"com.smart.cloud.api.auth.feign"})
// 如果认证模块已经引入，则不引入，使用LocalApi
@ConditionalOnMissingClass("com.smart.module.auth.SmartAuthSecurityAutoConfiguration")
public class SmartCloudAuthApiAutoConfiguration {
}
