package com.smart.monitor.server.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.monitor.server.core.event.EventCode;
import com.smart.monitor.server.core.event.MonitorEvent;
import com.smart.monitor.server.core.event.store.MonitorEventStore;
import com.smart.monitor.server.manager.mapper.MonitorEventMapper;
import com.smart.monitor.server.manager.model.MonitorEventPO;
import com.smart.monitor.server.manager.service.MonitorEventService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
* monitor_event - 事件信息 Service实现类
* @author GCCodeGenerator
* 2022-2-18
*/
@Service
public class MonitorEventServiceImpl extends BaseServiceImpl<MonitorEventMapper, MonitorEventPO> implements MonitorEventService, MonitorEventStore {

    @Override
    public void serialize(MonitorEvent<?> event) {
        String eventMessage = null;
        if (event.getData() != null) {
            if (event.getData() instanceof Throwable) {
                eventMessage = ((Throwable) event.getData()).getMessage();
            } else {
                eventMessage = JsonUtils.toJsonString(event.getData());
            }
        }
        final MonitorEventPO monitorEvent = MonitorEventPO.builder()
                .applicationCode(event.getClientData().getApplication().getApplicationName())
                .clientId(event.getClientData().getId().getValue())
                .eventCode(event.getCode().getCode())
                .timestamp(event.getTimestamp())
                .eventMessage(eventMessage)
                .createTime(LocalDateTime.now())
                .build();
        this.save(monitorEvent);
    }

    @Override
    public List<MonitorEventPO> list(@Nullable String applicationCode, @Nullable String clientId, @Nullable EventCode eventCode) {
        LambdaQueryWrapper<MonitorEventPO> queryWrapper = new QueryWrapper<MonitorEventPO>().lambda();
        if (StringUtils.hasLength(applicationCode)) {
            queryWrapper.eq(MonitorEventPO :: getApplicationCode, applicationCode);
        }
        if (StringUtils.hasLength(clientId)) {
            queryWrapper.eq(MonitorEventPO :: getClientId, clientId);
        }
        if (eventCode != null) {
            queryWrapper.eq(MonitorEventPO :: getEventCode, eventCode.getCode());
        }
        return this.list(queryWrapper);
    }
}
