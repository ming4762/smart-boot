package com.smart.monitor.autoconfigure.actuator.druid;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.smart.monitor.actuator.druid.SmartMonitorActuatorDruid;
import com.smart.monitor.actuator.druid.points.DruidSqlEndPoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/4/25 9:19
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({
        DruidActuatorBeanAutoConfiguration.class,
        DruidDataSourceAutoConfigure.class
})
@ConditionalOnClass(SmartMonitorActuatorDruid.class)
@ConditionalOnAvailableEndpoint(endpoint = DruidSqlEndPoint.class)
public class DruidSqlEndPointAutoConfiguration {

    /**
     * 创建druid sql端点
     * @return DruidSqlEndPoint
     */
    @Bean
    @ConditionalOnBean(StatFilter.class)
    public DruidSqlEndPoint druidSqlEndPoint() {
        return new DruidSqlEndPoint();
    }
}
