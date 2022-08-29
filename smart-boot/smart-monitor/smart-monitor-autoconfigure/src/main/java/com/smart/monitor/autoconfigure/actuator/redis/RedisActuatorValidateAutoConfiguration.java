package com.smart.monitor.autoconfigure.actuator.redis;

import com.smart.monitor.actuator.redis.RedisBeanValidator;
import com.smart.monitor.actuator.redis.SmartMonitorActuatorRedis;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2022/8/29 10:16
 * @since 1.0
 */
@ConditionalOnClass(SmartMonitorActuatorRedis.class)
@Configuration(proxyBeanMethods = false)
public class RedisActuatorValidateAutoConfiguration {

    @Bean
    public RedisBeanValidator beanValidator() {
        return new RedisBeanValidator();
    }
}
