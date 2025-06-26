package com.smart.cloud.api.system;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/3/8
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan
@EnableFeignClients(basePackages = {"com.smart.cloud.api.system.feign"})
// 如果系统模块已经引入，则不引入，使用LocalApi
@ConditionalOnMissingClass("com.smart.module.system.SmartSystemAutoConfiguration")
public class SmartCloudSystemApiAutoConfiguration {
}
