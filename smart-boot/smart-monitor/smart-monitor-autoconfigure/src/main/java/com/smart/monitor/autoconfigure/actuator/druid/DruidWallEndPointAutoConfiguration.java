package com.smart.monitor.autoconfigure.actuator.druid;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.wall.WallFilter;
import com.smart.monitor.actuator.druid.SmartMonitorActuatorDruid;
import com.smart.monitor.actuator.druid.points.DruidWallEndPoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/4/25 9:17
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({
        DruidActuatorBeanAutoConfiguration.class,
        DruidDataSourceAutoConfigure.class
})
@ConditionalOnClass(SmartMonitorActuatorDruid.class)
@ConditionalOnAvailableEndpoint(endpoint = DruidWallEndPoint.class)
public class DruidWallEndPointAutoConfiguration {

    /**
     * druid 防火墙端点
     * @return DruidWallEndPoint
     */
    @Bean
    @ConditionalOnBean(WallFilter.class)
    public DruidWallEndPoint druidWallEndPoint() {
        return new DruidWallEndPoint();
    }
}
