package com.smart.boot.autoconfigure.auth.extension;

import com.smart.auth.extensions.access.secret.AuthAccessSecretSecurityConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2024/11/6 15:46
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AuthAccessSecretSecurityConfigurer.class)
public class SmartAuthAccessSecretAutoConfiguration {
}
