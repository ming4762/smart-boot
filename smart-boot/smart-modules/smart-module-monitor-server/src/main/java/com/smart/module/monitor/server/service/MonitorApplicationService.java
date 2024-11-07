package com.smart.module.monitor.server.service;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.monitor.server.model.MonitorApplicationPO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author shizhongming
 * 2021/3/13 8:48 下午
 */
public interface MonitorApplicationService extends BaseService<MonitorApplicationPO> {

    /**
     * 查询人员关联的应用名字
     * @param userId 用户ID
     * @return application name List
     */
    List<String> listApplicationNameByUser(@NonNull Long userId);
}
