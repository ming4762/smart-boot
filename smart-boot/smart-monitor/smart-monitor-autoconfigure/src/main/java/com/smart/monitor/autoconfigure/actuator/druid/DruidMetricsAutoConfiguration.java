package com.smart.monitor.autoconfigure.actuator.druid;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.smart.monitor.actuator.druid.SmartMonitorActuatorDruid;
import com.smart.monitor.actuator.druid.meter.DruidDatasourceMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/4/25 9:17
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({
        DruidDataSourceAutoConfigure.class
})
@ConditionalOnClass(SmartMonitorActuatorDruid.class)
public class DruidMetricsAutoConfiguration {

    /**
     * 数据验指标配置
     * @return DruidDatasourceMetrics
     */
    @Bean
    @ConditionalOnMissingBean
    public DruidDatasourceMetrics druidDatasourceMetrics(MeterRegistry registry) {
        return new DruidDatasourceMetrics(registry);
    }
}
