package com.smart.framework.commons.core.log;

import com.smart.framework.commons.core.event.SmartCommonEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * 日志事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-12 21:59
 * @since 5.0.0
 */
@Getter
@Setter
public class SmartSaveLogEvent extends SmartCommonEvent {

    private SysLogSaveDTO logData;

}
