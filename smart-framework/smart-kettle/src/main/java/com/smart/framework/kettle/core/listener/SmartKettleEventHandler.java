package com.smart.framework.kettle.core.listener;

import com.smart.framework.kettle.core.listener.event.*;

/**
 * kettle事件接受接口
 * @author shizhongming
 * 2025/10/14 13:49
 * @since 5.0.0
 */
public interface SmartKettleEventHandler {

    /**
     * 作业完成事件处理
     * @param event 作业完成事件
     */
    default void handleJobFinished(SmartKettleJobFinishedEvent event) {
        // 作业完成事件处理
    }

    /**
     * 作业开始事件处理
     * @param event 作业开始事件
     */
    default void handleJobStarted(SmartKettleJobStartedEvent event) {
        // 作业开始事件处理
    }

    /**
     * 转换开始事件处理
     * @param event 转换开始事件
     */
    default void handleTransStarted(SmartKettleTransStartedEvent event) {
        // 转换开始事件处理
    }

    /**
     * 转换激活事件处理
     * @param event 转换激活事件
     */
    default void handleTransActive(SmartKettleTransActiveEvent event) {
        // 转换激活事件处理
    }

    /**
     * 转换完成事件处理
     * @param event 转换完成事件
     */
    default void handleTransFinished(SmartKettleTransFinishedEvent event) {
        // 转换完成事件处理
    }

    /**
     * 转换停止事件处理
     * @param event 转换停止事件
     */
    default void handleTransStopped(SmartKettleTransStoppedEvent event) {
        // 转换停止事件处理
    }
}
