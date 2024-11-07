package com.smart.boot.actuate.autoconfigure.druid;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import com.smart.boot.actuate.druid.SmartMonitorActuatorDruid;
import com.smart.boot.actuate.druid.points.DruidDataSourceEndPoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DruidDataSourceEndPoint
 * @author ShiZhongMing
 * 2021/4/25 9:12DruidDataSourceEndPoint
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({
        DruidActuatorBeanAutoConfiguration.class,
        DruidDataSourceAutoConfigure.class
})
@ConditionalOnClass(SmartMonitorActuatorDruid.class)
@ConditionalOnAvailableEndpoint(endpoint = DruidDataSourceEndPoint.class)
public class DruidDataSourceEndPointAutoConfiguration {

    /**
     * 数据源端点配置
     * @return DataSourceEndPoint
     */
    @Bean
    @ConditionalOnMissingBean
    public DruidDataSourceEndPoint dataSourceEndPoint() {
        return new DruidDataSourceEndPoint();
    }
}
