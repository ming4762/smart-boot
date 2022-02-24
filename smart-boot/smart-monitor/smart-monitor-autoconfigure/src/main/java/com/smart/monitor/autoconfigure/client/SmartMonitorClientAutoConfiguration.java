package com.smart.monitor.autoconfigure.client;

import com.smart.commons.core.spring.EnableRest;
import com.smart.monitor.client.core.SmartMonitorClient;
import com.smart.monitor.client.core.application.ApplicationFactory;
import com.smart.monitor.client.core.application.DefaultApplicationFactoryImpl;
import com.smart.monitor.client.core.properties.ClientProperties;
import com.smart.monitor.client.core.registration.*;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author ShiZhongMing
 * 2022/2/8
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SmartMonitorClient.class)
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "smart.monitor.client", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(ClientProperties.class)
@EnableRest
public class SmartMonitorClientAutoConfiguration {

    /**
     * 创建客户端工厂
     */
    @Bean
    @ConditionalOnMissingBean
    public ApplicationFactory applicationFactory(ClientProperties clientProperties, ServerProperties serverProperties, WebEndpointProperties properties) {
        return new DefaultApplicationFactoryImpl(clientProperties, serverProperties, properties);
    }

    /**
     * 客户端执行注册的类
     */
    @Bean
    @ConditionalOnMissingBean
    public RegistrarClient registrarClient() {
        return new RestRegistrarClientImpl();
    }

    /**
     * 客户端注册管理类
     */
    @Bean
    @ConditionalOnMissingBean(ApplicationRegistrar.class)
    public ApplicationRegistrar applicationRegistrar(ApplicationFactory applicationFactory, ClientProperties clientProperties, RegistrarClient registrarClient) {
        return new DefaultApplicationRegistrarImpl(
                applicationFactory,
                Arrays.asList(clientProperties.getRegistration().getUrl().split(",")),
                clientProperties.getRegistration().getOnce(),
                registrarClient
        );
    }

    @Bean
    public RegistrationApplicationListener registrationApplicationListener(ApplicationRegistrar applicationRegistrar, ClientProperties clientProperties) {
        return new RegistrationApplicationListener(applicationRegistrar, clientProperties);
    }
}
