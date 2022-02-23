package com.smart.monitor.autoconfigure.actuator.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.smart.monitor.actuator.druid.SmartMonitorActuatorDruid;
import com.smart.monitor.actuator.druid.filter.EnhancedStatFilter;
import com.smart.monitor.actuator.druid.support.MemorySlowSqlStore;
import com.smart.monitor.actuator.druid.support.Slf4jSlowSqlHandler;
import com.smart.monitor.actuator.druid.support.SlowSqlStore;
import com.smart.monitor.actuator.druid.support.StoreSlowSqlHandler;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2022/2/22
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({
        DruidDataSource.class,
        SmartMonitorActuatorDruid.class
})
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
public class DruidActuatorBeanAutoConfiguration {

    private static final String FILTER_STAT_PREFIX = "spring.datasource.druid.filter.stat";

    @Bean
    @ConfigurationProperties(FILTER_STAT_PREFIX)
    @ConditionalOnMissingBean
    public EnhancedStatFilter enhancedStatFilter() {
        EnhancedStatFilter enhancedStatFilter = new EnhancedStatFilter();
        enhancedStatFilter.setLogSlowSql(false);
        return enhancedStatFilter;
    }

    @Bean
    @ConditionalOnMissingBean
    public Slf4jSlowSqlHandler slf4jSlowSqlHandler() {
        return new Slf4jSlowSqlHandler();
    }

    /**
     * 内存慢SQL存储
     * @return 内存慢SQL存储
     */
    @Bean
    @ConditionalOnMissingBean(SlowSqlStore.class)
    public SlowSqlStore memorySlowSqlStore() {
        return new MemorySlowSqlStore();
    }

    @Bean
    @ConditionalOnMissingBean
    public StoreSlowSqlHandler memorySlowSqlHandler(SlowSqlStore slowSqlStore) {
        return new StoreSlowSqlHandler(slowSqlStore);
    }
}
