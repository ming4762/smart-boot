package com.smart.cloud.api.message;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 消息模块自动配置类
 * @author zhongming4762
 * 2023/6/6
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan
@EnableFeignClients(basePackages = {"com.smart.cloud.api.message.feign"})
// 如果消息模块已经引入，则不引入，使用LocalApi
@ConditionalOnMissingClass("com.smart.module.message.SmartMessageManagerAutoConfiguration")
public class SmartCloudMessageApiAutoConfiguration {
}
