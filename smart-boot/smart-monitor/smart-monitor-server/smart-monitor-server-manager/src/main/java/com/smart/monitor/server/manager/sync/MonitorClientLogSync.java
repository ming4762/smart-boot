package com.smart.monitor.server.manager.sync;

import com.google.common.collect.Maps;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.monitor.actuator.logback.data.LoggingEventData;
import com.smart.monitor.actuator.logback.utils.LogbackLogUtils;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.common.sync.MonitorDataSync;
import com.smart.monitor.server.core.client.request.ClientWebProxy;
import com.smart.monitor.server.manager.model.MonitorClientLogPO;
import com.smart.monitor.server.manager.service.MonitorClientLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 客户端日志同步类
 * @author ShiZhongMing
 * 2022/8/19
 * @since 3.0.0
 */
@Slf4j
public class MonitorClientLogSync implements MonitorDataSync {

    private static final String END_POINT = "logback";

    private final ClientWebProxy clientWebProxy;

    private final MonitorClientLogService monitorClientLogService;

    public MonitorClientLogSync(ClientWebProxy clientWebProxy, MonitorClientLogService monitorClientLogService) {
        this.clientWebProxy = clientWebProxy;
        this.monitorClientLogService = monitorClientLogService;
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
        this.monitorClientLogService.saveBatch(logList);
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
}
