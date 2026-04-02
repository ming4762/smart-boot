package com.smart.boot.auth.common;

import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.token.CompositeSmartTokenRepository;
import com.smart.framework.auth.core.token.SmartTokenRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author shizhongming
 * 2024/11/6 15:46
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AuthProperties.class)
public class SmartAuthCommonAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public CompositeSmartTokenRepository compositeSmartTokenRepository(List<SmartTokenRepository> smartTokenRepositoryList) {
        return new CompositeSmartTokenRepository(smartTokenRepositoryList.stream().map(SmartTokenRepository.class::cast).toList());
    }
}
