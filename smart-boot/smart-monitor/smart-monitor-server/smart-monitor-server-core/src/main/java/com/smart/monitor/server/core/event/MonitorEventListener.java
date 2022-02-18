package com.smart.monitor.server.core.event;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author ShiZhongMing
 * 2021/4/25 15:32
 * @since 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EventListener
public @interface MonitorEventListener {

    @AliasFor(annotation = EventListener.class)
    Class<?>[] value() default { MonitorEvent.class };

    @AliasFor(annotation = EventListener.class)
    Class<?>[] classes() default { MonitorEvent.class };

    @AliasFor(annotation = EventListener.class)
    String condition() default "";

    @AliasFor(annotation = EventListener.class)
    String id() default "";
}
