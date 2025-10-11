package com.smart.boot.autoconfigure.common;

import com.smart.framework.commons.core.trace.SmartTraceUtils;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

/**
 * 自动配置类，用于配置 TraceId
 * @author shizhongming
 * 2025/10/9 19:06
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SmartTraceAutoConfiguration implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof Tracer tracer) {
            SmartTraceUtils.setTracer(tracer);
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
