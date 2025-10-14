package com.smart.boot.autoconfigure.kettle;

import com.smart.framework.kettle.core.KettleActuator;
import com.smart.framework.kettle.core.KettleProperties;
import com.smart.framework.kettle.core.listener.SmartKettleEventGlobalHandler;
import com.smart.framework.kettle.core.log.KettleLogController;
import com.smart.framework.kettle.core.log.modifier.LogModifierHandler;
import com.smart.framework.kettle.core.repository.pool.KettleDatabaseRepositoryProvider;
import com.smart.framework.kettle.core.service.KettleService;
import com.smart.framework.kettle.core.service.KettleServiceImpl;
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
@ConditionalOnClass(KettleActuator.class)
public class SmartKettleAutoConfiguration {


    @Bean
    @ConfigurationProperties("smart.kettle")
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
    public KettleService kettleService(KettleDatabaseRepositoryProvider provider, KettleLogController kettleLogController, KettleProperties kettleProperties) {
        return new KettleServiceImpl(provider, kettleLogController, kettleProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmartKettleEventGlobalHandler smartKettleEventGlobalHandler() {
        return new SmartKettleEventGlobalHandler();
    }
}
