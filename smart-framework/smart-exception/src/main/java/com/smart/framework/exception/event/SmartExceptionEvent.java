package com.smart.framework.exception.event;

import com.smart.framework.commons.core.event.AbstractSmartCommonEvent;
import com.smart.framework.commons.core.exception.SmartExceptionEventData;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 系统异常事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-17 22:46
 * @since 5.0.0
 */
@Getter
@NoArgsConstructor
public class SmartExceptionEvent extends AbstractSmartCommonEvent {

    private SmartExceptionEventData data;

    public SmartExceptionEvent(SmartExceptionEventData data) {
        this.data = data;
    }
}
