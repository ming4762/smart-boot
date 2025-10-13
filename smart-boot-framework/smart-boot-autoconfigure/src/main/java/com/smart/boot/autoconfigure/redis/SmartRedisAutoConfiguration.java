package com.smart.boot.autoconfigure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.boot.autoconfigure.cache.SmartCacheProperties;
import com.smart.boot.autoconfigure.common.SmartWorkIdProperties;
import com.smart.boot.autoconfigure.common.SnowflakeWorkIdAllocatorConfiguration;
import com.smart.boot.autoconfigure.redis.customizer.JacksonRedissonAutoConfigurationCustomizer;
import com.smart.framework.commons.core.lock.limit.RateLimitService;
import com.smart.framework.commons.core.utils.snowflake.SnowflakeWorkIdAllocator;
import com.smart.framework.redis.service.RedisRateLimitServiceImpl;
import com.smart.framework.redis.service.RedisService;
import com.smart.framework.redis.service.RedisServiceImpl;
import com.smart.framework.redis.snowflake.RedisSnowflakeWorkIdAllocator;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redis配置类
 * @author shizhongming
 * 2020/1/17 8:45 下午
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({RedisAutoConfiguration.class, SnowflakeWorkIdAllocatorConfiguration.class})
@ConditionalOnClass(RedisService.class)
@EnableConfigurationProperties({ SmartCacheProperties.class })
public class SmartRedisAutoConfiguration {

    @Bean("redisService")
    @ConditionalOnMissingBean
    public RedisService redisService(@Autowired RedissonClient redissonClient, SmartCacheProperties smartCacheProperties) {
        return new RedisServiceImpl(smartCacheProperties.getPrefix(), redissonClient);
    }

    @Bean("redisRateLimitService")
    @ConditionalOnMissingBean(RateLimitService.class)
    public RateLimitService redisRateLimitService(RedisService redisService) {
        return new RedisRateLimitServiceImpl(redisService);
    }

    /**
     * radisson 使用jackson作为序列化工具
     * @return JacksonRedissonAutoConfigurationCustomizer
     */
//    @Bean
    public JacksonRedissonAutoConfigurationCustomizer jacksonRedissonAutoConfigurationCustomizer(@Autowired(required = false) ObjectMapper objectMapper) {
        return new JacksonRedissonAutoConfigurationCustomizer(objectMapper);
    }
    /**
     * 雪花算法工作ID分配器
     * @param redisService redis服务
     * @return SnowflakeWorkIdAllocator
     */
    @Bean
    @ConditionalOnMissingBean
    public SnowflakeWorkIdAllocator redisSnowflakeWorkIdAllocator(SmartWorkIdProperties properties, RedisService redisService) {
        return new RedisSnowflakeWorkIdAllocator(properties.getRedis().getWorkspace(), redisService);
    }
}
