package com.smart.starter.redis;

import com.smart.commons.core.lock.limit.RateLimitService;
import com.smart.starter.redis.service.RedisRateLimitServiceImpl;
import com.smart.starter.redis.service.RedisService;
import com.smart.starter.redis.service.RedisServiceImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redis配置类
 * @author shizhongming
 * 2020/1/17 8:45 下午
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class SmartRedisAutoConfiguration {

//    @Bean(name = "redisTemplate")
//    @ConditionalOnMissingBean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        var redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        // 设置key序列化器
//        var stringRedisSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//
//        var objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        // 指定序列化输入类型，类的信息也将添加到json中，这样才可以根据类名反序列化。
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.EVERYTHING);
//        // 添加java8时间类型转换器
//        objectMapper.registerModule(new JavaTimeModule());
//        var jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        return redisTemplate;
//    }

    @Bean("redisService")
    @ConditionalOnMissingBean
    public RedisService redisService(@Autowired RedissonClient redissonClient) {
        return new RedisServiceImpl(redissonClient);
    }

    @Bean("redisRateLimitService")
    @ConditionalOnMissingBean(RateLimitService.class)
    public RateLimitService redisRateLimitService(RedisService redisService) {
        return new RedisRateLimitServiceImpl(redisService);
    }
}
