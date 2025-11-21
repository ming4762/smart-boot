package com.smart.module.auth.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 认证服务管理自动配置类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/14 16:08
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan("com.smart.module.auth.server.manager")
public class SmartAuthServerManagerAutoConfiguration {
}
