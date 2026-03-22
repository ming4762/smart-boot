package com.smart.framework.commons.core.event;

/**
 * smart-boot 通用事件处理器
 * 监听AbstractSmartCommonEvent转为接口调用
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026/3/15 17:43
 * @since 5.0.0
 */
public interface SmartEventHandler<T extends AbstractSmartCommonEvent> {

    /**
     * 处理事件
     * @param event 事件
     */
    void handle(T event);
}
