package com.smart.module.auth.server;

import com.smart.module.auth.server.manager.service.Oauth2ClientService;
import com.smart.module.auth.server.sso.client.SmartDbRegisteredClientRepositoryImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * 认证服务SSO自动配置类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/20 14:19
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(SmartAuthServerManagerAutoConfiguration.class)
@ConditionalOnClass(RegisteredClientRepository.class)
public class SmartAuthServerSsoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RegisteredClientRepository.class)
    public SmartDbRegisteredClientRepositoryImpl smartDbRegisteredClientRepositoryImpl(Oauth2ClientService oauth2ClientService) {
        return new SmartDbRegisteredClientRepositoryImpl(oauth2ClientService);
    }
}
