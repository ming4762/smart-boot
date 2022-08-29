package com.smart.monitor.autoconfigure.actuator.redis;

import com.smart.monitor.actuator.redis.SmartMonitorActuatorRedis;
import com.smart.monitor.actuator.redis.points.RedisInfoEndPoint;
import com.smart.starter.redis.SmartRedisAutoConfiguration;
import com.smart.starter.redis.service.RedisService;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2022/2/24
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(SmartRedisAutoConfiguration.class)
@ConditionalOnClass(SmartMonitorActuatorRedis.class)
@ConditionalOnAvailableEndpoint(endpoint = RedisInfoEndPoint.class)
@ConditionalOnBean(RedisService.class)
public class RedisEndpointAutoConfiguration {

    @Bean
    public RedisInfoEndPoint redisInfoEndPoint(RedisService redisService) {
        return new RedisInfoEndPoint(redisService);
    }
}
