package com.smart.boot.actuate.autoconfigure.redis;

import com.smart.boot.actuate.redis.SmartMonitorActuatorRedis;
import com.smart.boot.actuate.redis.meter.RedisKeyMetrics;
import com.smart.boot.redis.SmartRedisAutoConfiguration;
import com.smart.framework.redis.service.RedisService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@ConditionalOnBean(RedisService.class)
public class RedisMetricsAutoConfiguration {

    @Bean
    public RedisKeyMetrics redisKeyMetrics(MeterRegistry registry, RedisService redisService) {
        return new RedisKeyMetrics(registry, redisService);
    }
}
