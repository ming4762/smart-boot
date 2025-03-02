package com.smart.boot.autoconfigure.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import com.smart.framework.druid.filter.stat.EnhancedStatFilter;
import com.smart.framework.druid.support.slow.Slf4jSlowSqlHandler;
import com.smart.framework.druid.support.slow.SlowSqlHandler;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid 自动配置
 * @author shizhongming
 * 2025/3/1 14:09
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({
        DruidDataSource.class
})
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
public class SmartDruidAutoConfiguration {

    private static final String FILTER_STAT_PREFIX = "spring.datasource.druid.filter.stat";

    @Bean
    @ConfigurationProperties(FILTER_STAT_PREFIX)
    @ConditionalOnProperty(prefix = FILTER_STAT_PREFIX, name = "enabled")
    @ConditionalOnMissingBean
    public EnhancedStatFilter enhancedStatFilter() {
        EnhancedStatFilter enhancedStatFilter = new EnhancedStatFilter();
        enhancedStatFilter.setLogSlowSql(false);
        return enhancedStatFilter;
    }

    @Bean
    @ConditionalOnMissingBean(SlowSqlHandler.class)
    public Slf4jSlowSqlHandler slf4jSlowSqlHandler() {
        return new Slf4jSlowSqlHandler();
    }

}
