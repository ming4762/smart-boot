package com.smart.commons.core.spring;

import com.smart.commons.core.exception.SystemException;
import com.smart.commons.core.lock.limit.RateLimitAspect;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * RateLimit 注册器
 * @author ShiZhongMing
 * 2022/8/26
 * @since 3.0.0
 */
public class RateLimitRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String REDIS_SERVICE_NAME = "redisRateLimitService";

    private static final String GUAVA_SERVICE_NAME = "guavaRateLimitService";

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry, @NonNull BeanNameGenerator importBeanNameGenerator) {
        var attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableRateLimit.class.getName()));
        if (attributes != null) {
            // 获取 rateLimitService name
            var serviceName = attributes.getString("service");
            if (!StringUtils.hasText(serviceName)) {
                // 如果没有指定serviceName，则自动获取serviceName
                serviceName = this.getDefaultServiceName(registry);
            }
            if (!StringUtils.hasText(serviceName)) {
                throw new SystemException("未找到RateLimit服务类，请检查serviceName是否正确，serviceName：" + serviceName);
            }
            // 构建 BeanDefinition
            var builder = BeanDefinitionBuilder.genericBeanDefinition(RateLimitAspect.class);
            builder.addPropertyValue("rateLimitService", new RuntimeBeanReference(serviceName));
            var beanDefinition = builder.getBeanDefinition();
            // 进行注册
            registry.registerBeanDefinition(importBeanNameGenerator.generateBeanName(beanDefinition, registry), beanDefinition);
        }
    }

    /**
     * 获取默认的服务名
     * @param registry BeanDefinitionRegistry
     * @return 默认的注册名
     */
    @Nullable
    private String getDefaultServiceName(BeanDefinitionRegistry registry) {
        var hasRedisClass = ClassUtils.isPresent("com.smart.starter.redis.service.RedisRateLimitServiceImpl", null);
        if (hasRedisClass) {
            return REDIS_SERVICE_NAME;
        }
        var hasGuavaClass = ClassUtils.isPresent("com.smart.starter.cache.guava.limit.GuavaRateLimitServiceImpl", null);
        if (hasGuavaClass) {
            return GUAVA_SERVICE_NAME;
        }
        return null;
    }

}
