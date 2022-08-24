package com.smart.monitor.server.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.monitor.server.manager.mapper.MonitorClientLogMapper;
import com.smart.monitor.server.manager.model.MonitorClientLogPO;
import com.smart.monitor.server.manager.service.MonitorApplicationService;
import com.smart.monitor.server.manager.service.MonitorClientLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* monitor_client_log - 客户端日志 Service实现类
* @author GCCodeGenerator
* 2022-3-3
*/
@Service
@Slf4j
public class MonitorClientLogServiceImpl extends BaseServiceImpl<MonitorClientLogMapper, MonitorClientLogPO> implements MonitorClientLogService {

    private final MonitorApplicationService monitorApplicationService;

    public MonitorClientLogServiceImpl(MonitorApplicationService monitorApplicationService) {
        this.monitorApplicationService = monitorApplicationService;
    }

    @Override
    public Integer sync(@NonNull ClientData clientData) {
        log.debug("client log sync start, application name: {}, client id: {}", clientData.getApplication().getApplicationName(), clientData.getId().getValue());
        Map<String, String> headers = Maps.newHashMap();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        AtomicInteger logNum = new AtomicInteger();
        this.clientWebProxy.forward(clientData.getId(), data -> ClientWebProxy.ForwardRequest.builder()
                .uri(clientData.getApplication().getEndPointUrl(END_POINT))
                .httpMethod(HttpMethod.POST)
                .httpHeaders(headers)
                .build(), result -> logNum.set(this.saveLog(result, clientData)), true, String.class
        );
        log.info("client log sync success, application name: {}, client id: {}, log num: {}", clientData.getApplication().getApplicationName(), clientData.getId().getValue(), logNum.get());
        return logNum.get();
    }

    private int saveLog(String data, @NonNull ClientData clientData) {
        final LocalDateTime currentTime = LocalDateTime.now();
        final List<LoggingEventData> eventDataList = JsonUtils.parseCollection(data, LoggingEventData.class);
        final List<MonitorClientLogPO> logList = eventDataList.stream().map(item -> MonitorClientLogPO.builder()
                .applicationCode(clientData.getApplication().getApplicationName())
                .clientId(clientData.getId().getValue())
                .threadName(item.getThreadName())
                .loggerName(item.getLoggerName())
                .timestamp(item.getTimestamp())
                .level(item.getLevel())
                .logText(LogbackLogUtils.convertToLogText(item))
                .createTime(currentTime)
                .build()).collect(Collectors.toList());
        this.saveBatch(logList);
        return logList.size();
    }

    @Override
    public boolean support(@NonNull ClientData clientData) {
        if (!clientData.getApplication().hasEndPoint(END_POINT)) {
            log.debug("client end point logback is not open, sync stop, application name: {}, client id: {}", clientData.getApplication().getApplicationName(), clientData.getId().getValue());
            return false;
        }
        return true;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public List<? extends MonitorClientLogPO> list(@NonNull QueryWrapper<MonitorClientLogPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.FILTER_BY_USER.name()))) {
            // 查询人员关联的客户端
            List<String> applicationCodeList = this.monitorApplicationService.listApplicationNameByUser(AuthUtils.getNonNullCurrentUserId());
            if (CollectionUtils.isEmpty(applicationCodeList)) {
                return new ArrayList<>(0);
            }
            queryWrapper.lambda().in(MonitorClientLogPO :: getApplicationCode, applicationCodeList);
        }
        return super.list(queryWrapper, parameter, paging);
    }
}
