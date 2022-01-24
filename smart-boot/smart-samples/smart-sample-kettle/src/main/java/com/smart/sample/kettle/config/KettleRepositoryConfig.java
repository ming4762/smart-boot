package com.smart.sample.kettle.config;

import com.smart.kettle.core.properties.KettleDatabaseRepositoryProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2022/1/22 10:42
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class KettleRepositoryConfig {


    @Bean
    @ConfigurationProperties("smart.kettle.repository")
    public KettleDatabaseRepositoryProperties kettleDatabaseRepositoryProperties() {
        return new KettleDatabaseRepositoryProperties();
    }
}
