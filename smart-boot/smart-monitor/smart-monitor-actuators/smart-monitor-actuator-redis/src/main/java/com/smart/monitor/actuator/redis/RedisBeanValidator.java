package com.smart.monitor.actuator.redis;

import com.smart.starter.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

/**
 * 验证REDIS actuator是否满足条件
 * @author ShiZhongMing
 * 2022/8/29
 * @since 3.0.0
 */
@Slf4j
public class RedisBeanValidator implements ApplicationContextAware {
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        boolean result = ClassUtils.isPresent("com.smart.starter.redis.service.RedisService", null);
        if (result) {
            try {
                applicationContext.getBean(RedisService.class);
            } catch (BeansException e) {
                result = false;
            }
        }
        if (!result) {
            log.warn("redis actuator无效，未找到RedisService，请导入包：{}:{}", "com.smart", "redis-spring-boot-starter");
        }

    }
}
