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
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
