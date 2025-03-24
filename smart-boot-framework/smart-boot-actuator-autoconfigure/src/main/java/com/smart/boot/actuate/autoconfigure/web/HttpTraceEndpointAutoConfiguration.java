package com.smart.boot.actuate.autoconfigure.web;

import com.smart.boot.actuate.web.exchanges.EnhanceHttpTraceEndpoint;
import com.smart.boot.actuate.web.exchanges.EnhanceHttpTraceRepository;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.web.exchanges.HttpExchangesEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2022/3/28
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnAvailableEndpoint(endpoint = HttpExchangesEndpoint.class)
public class HttpTraceEndpointAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public EnhanceHttpTraceEndpoint httpTraceEndpoint(EnhanceHttpTraceRepository httpTraceRepository) {
        return new EnhanceHttpTraceEndpoint(httpTraceRepository);
    }

    // TODO:待完善
//    @Bean
//    @ConditionalOnMissingBean
//    public EnhanceHttpTraceRepository enhanceHttpTraceRepository(ClientProperties clientProperties, WebEndpointProperties webEndpointProperties) {
//        List<String> excludeUrls = new java.util.ArrayList<>(List.of(webEndpointProperties.getBasePath() + "/**"));
//        if (StringUtils.isNotBlank(clientProperties.getHttpTrace().getExcludeUrls())) {
//            excludeUrls.addAll(
//                    Arrays.stream(clientProperties.getHttpTrace().getExcludeUrls().split(","))
//                            .map(String::trim)
//                            .filter(StringUtils::isNotBlank)
//                            .toList()
//            );
//        }
//        return new InMemoryEnhanceHttpTraceRepository(excludeUrls);
//    }
}
