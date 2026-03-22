package com.smart.framework.commons.core.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * smart-boot 通用事件监听器
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-15 17:47
 * @since 5.0.0
 */
@Slf4j
public class SmartEventListener implements ApplicationContextAware {

    private final Map<Class<?>, List<SmartEventHandler>> eventHandlerMap = new HashMap<>();

    @EventListener(AbstractSmartCommonEvent.class)
    public void dispatchEvent(AbstractSmartCommonEvent event) {
        List<SmartEventHandler> handlers = eventHandlerMap.get(event.getClass());
        if (CollectionUtils.isEmpty(handlers)) {
            return;
        }
        for (SmartEventHandler handler : handlers) {
            try {
                handler.handle(event);
            } catch (Exception e) {
                log.warn("事件处理异常", e);
            }
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        String[] beanNames = applicationContext.getBeanNamesForType(SmartEventHandler.class);
        for (String name : beanNames) {
            SmartEventHandler handler = applicationContext.getBean(name, SmartEventHandler.class);

            ResolvableType resolvableType = ResolvableType.forClass(handler.getClass())
                    .as(SmartEventHandler.class);

            Class<?> eventType = resolvableType.getGeneric(0).resolve();
            if (eventType == null) {
                eventType = AbstractSmartCommonEvent.class;
            }
            eventHandlerMap.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
        }
    }
}
