package com.smart.boot.actuate.autoconfigure.redis;

import com.smart.boot.actuate.redis.RedisBeanValidator;
import com.smart.boot.actuate.redis.SmartMonitorActuatorRedis;
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
