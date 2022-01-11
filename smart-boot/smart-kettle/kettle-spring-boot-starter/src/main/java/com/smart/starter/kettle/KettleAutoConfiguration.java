package com.smart.starter.kettle;

import com.smart.kettle.core.KettleProperties;
import com.smart.kettle.core.log.KettleLogController;
import com.smart.kettle.core.log.modifier.LogModifierHandler;
import com.smart.kettle.core.properties.KettleDatabaseRepositoryProperties;
import com.smart.kettle.core.repository.pool.KettleDatabaseRepositoryProvider;
import com.smart.kettle.core.service.KettleService;
import com.smart.kettle.core.service.KettleServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/7/15 11:49
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(KettleDatabaseRepositoryProperties.class)
public class KettleAutoConfiguration {

    @ConfigurationProperties("smart.kettle")
    @Bean
    public KettleProperties kettleProperties() {
        return new KettleProperties();
    }

    /**
     * 创建kettle数据库资源库提供器
     * @return KettleDatabaseRepositoryProvider
     */
    @Bean
    @ConditionalOnMissingBean
    public KettleDatabaseRepositoryProvider kettleDatabaseRepositoryProvider() {
        return new KettleDatabaseRepositoryProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public LogModifierHandler logModifierHandler() {
        return new LogModifierHandler();
    }

    /**
     * 创建kettle日志控制器
     * @param kettleProperties kettle参数
     * @return KettleLogController
     */
    @Bean
    @ConditionalOnMissingBean
    public KettleLogController kettleLogController(KettleProperties kettleProperties, LogModifierHandler logModifierHandler) {
        return new KettleLogController(kettleProperties.getLog(), logModifierHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public KettleService kettleService(KettleDatabaseRepositoryProvider provider, KettleLogController kettleLogController) {
        return new KettleServiceImpl(provider, kettleLogController);
    }
}
