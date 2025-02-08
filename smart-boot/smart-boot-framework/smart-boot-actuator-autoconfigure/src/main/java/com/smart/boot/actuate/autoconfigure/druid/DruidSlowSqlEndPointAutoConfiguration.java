package com.smart.boot.actuate.autoconfigure.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import com.smart.boot.actuate.druid.SmartMonitorActuatorDruid;
import com.smart.boot.actuate.druid.filter.EnhancedStatFilter;
import com.smart.boot.actuate.druid.points.DruidSlowSqlEndPoint;
import com.smart.boot.actuate.druid.support.SlowSqlStore;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/4/25 9:21
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({
        DruidActuatorBeanAutoConfiguration.class,
        DruidDataSourceAutoConfigure.class
})
@ConditionalOnClass({
        DruidDataSource.class,
        SmartMonitorActuatorDruid.class
})
@ConditionalOnAvailableEndpoint(endpoint = DruidSlowSqlEndPoint.class)
public class DruidSlowSqlEndPointAutoConfiguration {

    /**
     * 慢SQL端点
     * @param slowSqlStore 慢SQL存储器
     * @return DruidSlowSqlEndPoint
     */
    @Bean
    @ConditionalOnBean(EnhancedStatFilter.class)
    public DruidSlowSqlEndPoint druidSlowSqlEndPoint(SlowSqlStore slowSqlStore) {
        return new DruidSlowSqlEndPoint(slowSqlStore);
    }
}
