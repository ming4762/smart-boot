package com.smart.boot.autoconfigure.auth.session;

import com.smart.framework.auth.core.properties.AuthProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.session.data.redis.RedisSessionRepository;

/**
 * @author shizhongming
 * 2025/3/13 17:22
 * @since 5.0.0
 */
public class RedisSessionRepositoryBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (!(bean instanceof RedisSessionRepository redisSessionRepository)) {
            return bean;
        }
        AuthProperties authProperties = this.applicationContext.getBean(AuthProperties.class);
        redisSessionRepository.setRedisKeyNamespace(authProperties.getPrefix());
        return redisSessionRepository;
    }


    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
