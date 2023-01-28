package com.smart.monitor.autoconfigure.client;

import com.google.common.collect.Lists;
import com.smart.monitor.client.core.SmartMonitorClient;
import com.smart.monitor.client.core.actuator.points.EnhanceHttpTraceEndpoint;
import com.smart.monitor.client.core.properties.ClientProperties;
import com.smart.monitor.client.core.trace.http.EnhanceHttpTraceRepository;
import com.smart.monitor.client.core.trace.http.InMemoryEnhanceHttpTraceRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.web.exchanges.HttpExchangesEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/3/28
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SmartMonitorClient.class)
@ConditionalOnAvailableEndpoint(endpoint = HttpExchangesEndpoint.class)
public class HttpTraceEndpointAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public EnhanceHttpTraceEndpoint httpTraceEndpoint(EnhanceHttpTraceRepository httpTraceRepository) {
        return new EnhanceHttpTraceEndpoint(httpTraceRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public EnhanceHttpTraceRepository enhanceHttpTraceRepository(ClientProperties clientProperties, WebEndpointProperties webEndpointProperties) {
        List<String> excludeUrls = Lists.newArrayList(webEndpointProperties.getBasePath() + "/**");
        if (StringUtils.isNotBlank(clientProperties.getHttpTrace().getExcludeUrls())) {
            excludeUrls.addAll(
                    Arrays.stream(clientProperties.getHttpTrace().getExcludeUrls().split(","))
                            .map(String::trim)
                            .filter(StringUtils::isNotBlank)
                            .toList()
            );
        }
        return new InMemoryEnhanceHttpTraceRepository(excludeUrls);
    }
}
