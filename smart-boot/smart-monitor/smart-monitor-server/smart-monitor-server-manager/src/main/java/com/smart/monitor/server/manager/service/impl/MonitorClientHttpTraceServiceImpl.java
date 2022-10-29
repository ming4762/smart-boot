package com.smart.monitor.server.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.utils.DateUtils;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.common.sync.MonitorDataSync;
import com.smart.monitor.server.core.client.request.ClientWebProxy;
import com.smart.monitor.server.manager.mapper.MonitorClientHttpTraceMapper;
import com.smart.monitor.server.manager.model.MonitorClientHttpTracePO;
import com.smart.monitor.server.manager.service.MonitorApplicationService;
import com.smart.monitor.server.manager.service.MonitorClientHttpTraceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
* monitor_client_http_trace - 客户端HttpTrace Service实现类
* @author GCCodeGenerator
* 2022-3-25 13:19:44
*/
@Service
@Slf4j
public class MonitorClientHttpTraceServiceImpl extends BaseServiceImpl<MonitorClientHttpTraceMapper, MonitorClientHttpTracePO> implements MonitorClientHttpTraceService, MonitorDataSync {

    private static final String END_POINT = "httptrace";

    private final ClientWebProxy clientWebProxy;

    private final MonitorApplicationService monitorApplicationService;

    public MonitorClientHttpTraceServiceImpl(ClientWebProxy clientWebProxy, MonitorApplicationService monitorApplicationService) {
        this.clientWebProxy = clientWebProxy;
        this.monitorApplicationService = monitorApplicationService;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer sync(@NonNull ClientData clientData) {
        log.debug("client http trace sync start, application code: {}, client id: {}", clientData.getApplication().getApplicationName(), clientData.getId().getValue());
        Map<String, String> headers = Maps.newHashMap();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        AtomicInteger saveNum = new AtomicInteger();
        this.clientWebProxy.forward(clientData.getId(), repositoryData -> ClientWebProxy.ForwardRequest.builder()
                .uri(clientData.getApplication().getEndPointUrl("httptrace?clear=true"))
                .httpMethod(HttpMethod.GET)
                .httpHeaders(headers)
                .build(), result -> saveNum.set(this.saveHttpTrace((List<Map<String, Object>>) result, clientData)), true);
        log.info("client http trace sync success, application name: {}, client id: {}, log num: {}", clientData.getApplication().getApplicationName(), clientData.getId().getValue(), saveNum.get());
        return saveNum.get();
    }

    @SuppressWarnings("unchecked")
    private int saveHttpTrace(List<Map<String, Object>> data, @NonNull ClientData clientData) {
        final LocalDateTime currentTime = LocalDateTime.now();
        final List<MonitorClientHttpTracePO> httpTraceList = data.stream().map(item -> {
            final Map<String, Object> request = (Map<String, Object>) item.get("request");
            final Map<String, Object> response = (Map<String, Object>) item.get("response");
            // 获取时间戳
            Long timestamp = Optional.ofNullable((String) item.get("timestamp"))
                    .map(DateUtils::parseInstant)
                    .map(Instant::toEpochMilli)
                    .orElse(null);
            return MonitorClientHttpTracePO.builder()
                    .applicationCode(clientData.getApplication().getApplicationName())
                    .clientId(clientData.getId().getValue())
                    .createTime(currentTime)
                    .httpMethod((String) request.get("method"))
                    .timeTaken(Long.parseLong(item.get("timeTaken").toString()))
                    .url((String) request.get("uri"))
                    .responseStatus((Integer) response.get("status"))
                    .timestamp(timestamp)
                    .data(JsonUtils.toJsonString(item))
                    .build();
        }).toList();
        this.saveBatch(httpTraceList);
        return httpTraceList.size();
    }

    @Override
    public boolean support(@NonNull ClientData clientData) {
        if (!clientData.getApplication().hasEndPoint(END_POINT)) {
            log.debug("client end point httptrace is not open, sync stop, application code: {}, client id: {}", clientData.getApplication().getApplicationName(), clientData.getId().getValue());
            return false;
        }
        return true;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public List<? extends MonitorClientHttpTracePO> list(@NonNull QueryWrapper<MonitorClientHttpTracePO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.ASSIGN_FIELD.name()))) {
            queryWrapper.lambda().select(
                    MonitorClientHttpTracePO :: getId,
                    MonitorClientHttpTracePO :: getApplicationCode,
                    MonitorClientHttpTracePO :: getClientId,
                    MonitorClientHttpTracePO :: getCreateTime,
                    MonitorClientHttpTracePO :: getHttpMethod,
                    MonitorClientHttpTracePO :: getTimestamp,
                    MonitorClientHttpTracePO :: getTimeTaken,
                    MonitorClientHttpTracePO :: getUrl,
                    MonitorClientHttpTracePO :: getResponseStatus
            );
        }
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.FILTER_BY_USER.name()))) {
            Long userId = AuthUtils.getCurrentUserId();
            if (userId == null) {
                return new ArrayList<>(0);
            }
            if (!AuthUtils.isSuperAdmin()) {
                List<String> codeList = this.monitorApplicationService.listApplicationNameByUser(AuthUtils.getNonNullCurrentUserId());
                if (CollectionUtils.isEmpty(codeList)) {
                    return new ArrayList<>(0);
                }
                queryWrapper.lambda().in(MonitorClientHttpTracePO :: getApplicationCode, codeList);
            }
        }
        return super.list(queryWrapper, parameter, paging);
    }
}
