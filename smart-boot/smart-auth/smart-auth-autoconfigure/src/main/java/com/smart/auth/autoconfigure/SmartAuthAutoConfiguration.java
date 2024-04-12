package com.smart.auth.autoconfigure;

import com.smart.auth.core.authentication.DefaultSmartAuthenticationEventPublisher;
import com.smart.auth.core.authentication.SmartAuthenticationEventPublisher;
import com.smart.auth.core.event.AuthEventListener;
import com.smart.auth.core.token.TokenRepository;
import com.smart.auth.core.userdetails.DefaultUserDetailsBuilderImpl;
import com.smart.auth.core.userdetails.UserDetailsBuilder;
import com.smart.module.api.system.SystemAuthUserApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0.7
 */
@Configuration(proxyBeanMethods = false)
public class SmartAuthAutoConfiguration {

    /**
     * 创建认证事件监听器
     * @return 认证事件监听器
     */
    @Bean
    @ConditionalOnMissingBean(AuthEventListener.class)
    public AuthEventListener authEventListener() {
        return new AuthEventListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsBuilder userDetailsBuilder(SystemAuthUserApi systemAuthUserApi, List<TokenRepository> tokenRepositoryList) {
        return new DefaultUserDetailsBuilderImpl(systemAuthUserApi, tokenRepositoryList);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmartAuthenticationEventPublisher smartAuthenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultSmartAuthenticationEventPublisher(applicationEventPublisher);
    }

}
