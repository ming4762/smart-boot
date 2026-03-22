package com.smart.boot.autoconfigure.common;

import com.smart.framework.commons.core.trace.SmartTraceUtils;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置类，用于配置 TraceId
 * @author shizhongming
 * 2025/10/9 19:06
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SmartTraceAutoConfiguration {

    @Bean
    public ApplicationListener<ApplicationReadyEvent> traceListener(ObjectProvider<Tracer> tracerProvider) {
        return event -> {
            Tracer tracer = tracerProvider.getIfAvailable();
            if (tracer != null) {
                SmartTraceUtils.setTracer(tracer);
            }
        };
    }
}
