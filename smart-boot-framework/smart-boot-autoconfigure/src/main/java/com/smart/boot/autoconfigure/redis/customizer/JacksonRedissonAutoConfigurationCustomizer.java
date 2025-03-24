package com.smart.boot.autoconfigure.redis.customizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;

/**
 * radisson 使用jackson作为序列化工具
 * @author shizhongming
 * 2025/2/7 16:11
 * @since 5.0.0
 */
public class JacksonRedissonAutoConfigurationCustomizer implements RedissonAutoConfigurationCustomizer {

    private final ObjectMapper objectMapper;


    public JacksonRedissonAutoConfigurationCustomizer(ObjectMapper objectMapper) {
        if (objectMapper != null) {
            this.objectMapper = objectMapper.copy();
        } else {
            this.objectMapper = initObjectMapper();
        }
    }

    /**
     * Customize the RedissonClient configuration.
     *
     * @param configuration the {@link Config} to customize
     */
    @Override
    public void customize(Config configuration) {
        JsonJacksonCodec codec = new JsonJacksonCodec(this.objectMapper);
        configuration.setCodec(codec);
    }

    private ObjectMapper initObjectMapper() {
        return new ObjectMapper();
    }
}
