package com.smart.monitor.autoconfigure.client;

import com.smart.monitor.client.core.interceptor.MonitorAuthInterceptor;
import com.smart.monitor.client.core.properties.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/10/24
 * @since 3.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "smart.monitor.client.auth", name = "enabled", matchIfMissing = true)
@ConditionalOnWebApplication
@ConditionalOnBean({
        DispatcherServlet.class,
        WebEndpointsSupplier.class,
        SmartMonitorClientAutoConfiguration.class
})
@AutoConfigureAfter({
        WebEndpointAutoConfiguration.class,
        SmartMonitorClientAutoConfiguration.class
})
public class SmartMonitorClientEndPointAuthAutoConfiguration implements WebMvcConfigurer {

    private ClientProperties clientProperties;

    private WebEndpointProperties webEndpointProperties;

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
                                                                         ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier,
                                                                         EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties,
                                                                         WebEndpointProperties webEndpointProperties, Environment environment, @Autowired(required = false) MonitorAuthInterceptor monitorAuthInterceptor) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = StringUtils.hasText(basePath)
                || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT);
        WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping =  new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
                corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
                shouldRegisterLinksMapping);
        if (monitorAuthInterceptor != null) {
            webMvcEndpointHandlerMapping.setInterceptors(monitorAuthInterceptor);
        }
        return webMvcEndpointHandlerMapping;
    }

    @Bean
    @ConditionalOnMissingBean
    public MonitorAuthInterceptor monitorAuthInterceptor() {
        return new MonitorAuthInterceptor(this.clientProperties == null ? null : this.clientProperties.getAuth().getToken());
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        if (this.clientProperties != null && StringUtils.hasText(this.clientProperties.getAuth().getToken())) {
            registry.addInterceptor(monitorAuthInterceptor()).addPathPatterns(this.webEndpointProperties.getBasePath() + "/**");
        }
    }

    @Autowired(required = false)
    public void setClientProperties(ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    @Autowired(required = false)
    public void setWebEndpointProperties(WebEndpointProperties webEndpointProperties) {
        this.webEndpointProperties = webEndpointProperties;
    }
}
