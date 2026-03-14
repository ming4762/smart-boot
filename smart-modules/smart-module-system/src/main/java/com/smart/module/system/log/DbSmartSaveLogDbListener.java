package com.smart.module.system.log;

import com.smart.framework.commons.core.log.SmartSaveLogEvent;
import com.smart.framework.commons.core.log.SysLogSaveDTO;
import com.smart.module.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 监听日志保存事件，保存日志到数据库
 * @author ShiZhongMing
 * 2021/12/17
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class DbSmartSaveLogDbListener {

    private final SysLogService sysLogService;

    /**
     * 保存事件日志到数据库
     * @param event 事件
     */
    @EventListener(SmartSaveLogEvent.class)
    public void saveEventLog(SmartSaveLogEvent event) {
        SysLogSaveDTO dto = new SysLogSaveDTO();
        BeanUtils.copyProperties(event.getLogData(), dto);
        this.sysLogService.saveLog(dto);
    }
}
