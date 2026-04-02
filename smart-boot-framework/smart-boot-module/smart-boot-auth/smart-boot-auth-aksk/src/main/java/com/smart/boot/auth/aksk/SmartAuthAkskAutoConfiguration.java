package com.smart.boot.auth.aksk;

import com.smart.auth.extensions.aksk.AuthAkskSecurityConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2024/11/6 15:46
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AuthAkskSecurityConfigurer.class)
public class SmartAuthAkskAutoConfiguration {
}
