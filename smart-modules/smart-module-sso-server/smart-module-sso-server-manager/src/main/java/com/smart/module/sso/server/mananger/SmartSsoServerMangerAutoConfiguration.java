package com.smart.module.sso.server.mananger;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-19 12:20
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan({"com.smart.module.sso.server.mananger", "com.smart.module.sso.server.common.manager"})
public class SmartSsoServerMangerAutoConfiguration {
}
