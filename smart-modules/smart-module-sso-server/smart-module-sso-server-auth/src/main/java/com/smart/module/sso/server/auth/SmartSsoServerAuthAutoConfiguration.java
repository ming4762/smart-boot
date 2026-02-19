package com.smart.module.sso.server.auth;

import com.smart.module.sso.server.auth.client.SmartDbRegisteredClientRepositoryImpl;
import com.smart.module.sso.server.common.manager.service.SsoOauth2ClientService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 *
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-19 12:23
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RegisteredClientRepository.class)
@ComponentScan("com.smart.module.sso.server.common.manager")
@EnableConfigurationProperties(SmartSsoServerAuthProperties.class)
public class SmartSsoServerAuthAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RegisteredClientRepository.class)
    public SmartDbRegisteredClientRepositoryImpl smartDbRegisteredClientRepositoryImpl(SmartSsoServerAuthProperties ssoServerAuthProperties, SsoOauth2ClientService oauth2ClientService) {
        return new SmartDbRegisteredClientRepositoryImpl(ssoServerAuthProperties, oauth2ClientService);
    }
}
