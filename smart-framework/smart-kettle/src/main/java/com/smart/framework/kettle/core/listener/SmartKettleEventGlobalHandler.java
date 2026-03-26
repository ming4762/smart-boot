package com.smart.framework.kettle.core.listener;

import com.smart.framework.kettle.core.listener.event.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * kettle事件全局处理器
 * @author shizhongming
 * 2025/10/14 13:48
 * @since 5.0.0
 */
public class SmartKettleEventGlobalHandler implements ApplicationContextAware {

    private List<SmartKettleEventHandler> eventHandlerList;

    /**
     * 作业完成事件处理
     * @param event 作业完成事件
     */
    @EventListener(classes = SmartKettleJobFinishedEvent.class)
    public void handleJobFinished(SmartKettleJobFinishedEvent event) {
        this.eventHandlerList.forEach(handler -> handler.handleJobFinished(event));
    }
    /**
     * 作业开始事件处理
     * @param event 作业开始事件
     */
    @EventListener(classes = SmartKettleJobStartedEvent.class)
    public void handleJobStarted(SmartKettleJobStartedEvent event) {
        this.eventHandlerList.forEach(handler -> handler.handleJobStarted(event));
    }

    /**
     * 转换开始事件处理
     * @param event 转换开始事件
     */
    @EventListener(classes = SmartKettleTransStartedEvent.class)
    public void handleTransStarted(SmartKettleTransStartedEvent event) {
        this.eventHandlerList.forEach(handler -> handler.handleTransStarted(event));
    }

    /**
     * 转换完成事件处理
     * @param event 转换完成事件
     */
    @EventListener(classes = SmartKettleTransFinishedEvent.class)
    public void handleTransFinished(SmartKettleTransFinishedEvent event) {
        this.eventHandlerList.forEach(handler -> handler.handleTransFinished(event));
    }

    /**
     * 转换激活事件处理
     * @param event 转换激活事件
     */
    @EventListener(classes = SmartKettleTransActiveEvent.class)
    public void handleTransActive(SmartKettleTransActiveEvent event) {
        this.eventHandlerList.forEach(handler -> handler.handleTransActive(event));
    }

    /**
     * 转换停止事件处理
     * @param event 转换停止事件
     */
    @EventListener(classes = SmartKettleTransStoppedEvent.class)
    public void handleTransStopped(SmartKettleTransStoppedEvent event) {
        this.eventHandlerList.forEach(handler -> handler.handleTransStopped(event));
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.eventHandlerList = applicationContext.getBeansOfType(SmartKettleEventHandler.class).values().stream().toList();
    }
}
