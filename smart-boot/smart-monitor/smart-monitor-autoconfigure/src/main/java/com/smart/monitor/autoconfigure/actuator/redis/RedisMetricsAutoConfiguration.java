package com.smart.monitor.autoconfigure.actuator.redis;

import com.smart.monitor.actuator.redis.SmartMonitorActuatorRedis;
import com.smart.monitor.actuator.redis.meter.RedisKeyMetrics;
import com.smart.starter.redis.SmartRedisAutoConfiguration;
import com.smart.starter.redis.service.RedisService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2022/2/25
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SmartMonitorActuatorRedis.class)
@AutoConfigureAfter(SmartRedisAutoConfiguration.class)
public class RedisMetricsAutoConfiguration {

    @Bean
    public RedisKeyMetrics redisKeyMetrics(MeterRegistry registry, RedisService redisService) {
        return new RedisKeyMetrics(registry, redisService);
    }
}
