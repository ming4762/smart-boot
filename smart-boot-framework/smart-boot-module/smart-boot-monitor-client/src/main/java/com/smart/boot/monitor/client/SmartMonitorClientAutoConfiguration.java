package com.smart.boot.monitor.client;

import com.smart.framework.commons.core.spring.EnableRest;
import com.smart.framework.monitor.client.SmartMonitorClient;
import com.smart.framework.monitor.client.application.ApplicationFactory;
import com.smart.framework.monitor.client.application.DefaultApplicationFactoryImpl;
import com.smart.framework.monitor.client.properties.ClientProperties;
import com.smart.framework.monitor.client.registration.*;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
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
    public ApplicationFactory applicationFactory(ClientProperties clientProperties, WebEndpointProperties properties) {
        return new DefaultApplicationFactoryImpl(clientProperties, properties.getBasePath());
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
