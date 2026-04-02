package com.smart.boot.monitor.server;

import com.smart.boot.redis.SmartRedisAutoConfiguration;
import com.smart.framework.monitor.server.SmartMonitorServer;
import com.smart.framework.monitor.server.client.repository.ClientRepository;
import com.smart.framework.monitor.server.client.repository.RedisClientRepositoryImpl;
import com.smart.framework.monitor.server.common.MonitorServerProperties;
import com.smart.framework.redis.service.RedisService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2022/10/26
 * @since 3.0.0
 */
@Configuration
@AutoConfigureAfter(SmartRedisAutoConfiguration.class)
@AutoConfigureBefore(MonitorServerAutoConfiguration.class)
@ConditionalOnBean(RedisService.class)
@ConditionalOnClass(SmartMonitorServer.class)
public class MonitorServerRedisClientRepositoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ClientRepository redisClientRepository(RedisService redisService, MonitorServerProperties properties) {
        return new RedisClientRepositoryImpl(redisService, properties);
    }
}
