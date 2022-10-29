package com.smart.monitor.server.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.common.sync.MonitorDataSync;
import com.smart.monitor.server.core.client.request.ClientWebProxy;
import com.smart.monitor.server.manager.mapper.MonitorClientSlowSqlMapper;
import com.smart.monitor.server.manager.model.MonitorClientSlowSqlPO;
import com.smart.monitor.server.manager.service.MonitorApplicationService;
import com.smart.monitor.server.manager.service.MonitorClientSlowSqlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
* monitor_client_slow_sql - 客户端慢SQL Service实现类
* @author GCCodeGenerator
* 2022-2-24 10:57:04
*/
@Service
@Slf4j
public class MonitorClientSlowSqlServiceImpl extends BaseServiceImpl<MonitorClientSlowSqlMapper, MonitorClientSlowSqlPO> implements MonitorClientSlowSqlService, MonitorDataSync {

    /**
     * SLOW SQL端点
     */
    private static final String END_POINT = "druidSlowSql";

    private final ClientWebProxy clientWebProxy;

    private final MonitorApplicationService monitorApplicationService;

    public MonitorClientSlowSqlServiceImpl(ClientWebProxy clientWebProxy, MonitorApplicationService monitorApplicationService) {
        this.clientWebProxy = clientWebProxy;
        this.monitorApplicationService = monitorApplicationService;
    }

    @Override
    public Integer sync(@NonNull ClientData clientData) {
        log.debug("client slow log sync start, application name: {}, client id: {}", clientData.getApplication().getApplicationName(), clientData.getId().getValue());
        Map<String, String> headers = Maps.newHashMap();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        AtomicInteger saveNum = new AtomicInteger();
        this.clientWebProxy.forward(clientData.getId(), repositoryData -> ClientWebProxy.ForwardRequest.builder()
                .uri(clientData.getApplication().getManagementUrl() + "/druidSlowSql?clear=true")
                .httpMethod(HttpMethod.GET)
                .httpHeaders(headers)
                .build(), result -> saveNum.set(this.saveSlowSql(result, clientData)), true

        );
        log.info("client slow log sync success, application name: {}, client id: {}, log num: {}", clientData.getApplication().getApplicationName(), clientData.getId().getValue(), saveNum.get());
        return saveNum.get();
    }

    /**
     * 执行保存操作
     * @param data 需要保存的数据
     * @param clientData 客户端数据
     * @return 保存的条数
     */
    private int saveSlowSql(Object data, @NonNull ClientData clientData) {
        final List<Map<String, Object>> dataList = (List<Map<String, Object>>) data;
        final List<MonitorClientSlowSqlPO> clientSlowSqlList = dataList.stream().map(item -> MonitorClientSlowSqlPO.builder()
                .applicationCode(clientData.getApplication().getApplicationName())
                .clientId(clientData.getId().getValue())
                .clientUrl(clientData.getApplication().getClientUrl())
                .sqlId(Long.parseLong(item.get("id").toString()))
                .sqlText((String) item.get("sql"))
                .parameter((String) item.get("parameter"))
                .useMillis(Long.parseLong(item.get("useMillis").toString()))
                .datasourceName((String) item.get("datasourceName"))
                .timestamp((Long) item.get("timestamp"))
                .build()).toList();
        this.saveBatch(clientSlowSqlList);
        return clientSlowSqlList.size();
    }

    @Override
    public boolean support(@NonNull ClientData clientData) {
        if (!clientData.getApplication().hasEndPoint(END_POINT)) {
            log.debug("client end point druidSlowSql is not open, sync stop, application name: {}, client id: {}", clientData.getApplication().getApplicationName(), clientData.getId().getValue());
            return false;
        }
        return true;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public List<? extends MonitorClientSlowSqlPO> list(@NonNull QueryWrapper<MonitorClientSlowSqlPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.FILTER_BY_USER.name()))) {
            // 查询人员关联的APP
            List<String> appNameList = this.monitorApplicationService.listApplicationNameByUser(AuthUtils.getNonNullCurrentUserId());
            if (CollectionUtils.isEmpty(appNameList)) {
                return new ArrayList<>(0);
            }
            queryWrapper.lambda().in(MonitorClientSlowSqlPO :: getApplicationCode, appNameList);
        }
        return super.list(queryWrapper, parameter, paging);
    }
}
