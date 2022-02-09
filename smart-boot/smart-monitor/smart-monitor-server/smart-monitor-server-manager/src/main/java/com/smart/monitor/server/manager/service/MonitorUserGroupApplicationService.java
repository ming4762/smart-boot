package com.smart.monitor.server.manager.service;

import com.smart.crud.service.BaseService;
import com.smart.monitor.server.manager.model.MonitorUserGroupApplicationPO;
import com.smart.monitor.server.manager.pojo.dto.MonitorApplicationSetUserGroupDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/2/9
 * @since 2.0.0
 */
public interface MonitorUserGroupApplicationService extends BaseService<MonitorUserGroupApplicationPO> {

    /**
     * 查询用户关联的应用ID
     * @param userId 用户ID
     * @return 应用ID列表
     */
    List<Long> listApplicationIdByUserId(@NonNull Long userId);

    /**
     * 设置用户关联的用户组
     * @param parameter 参数
     * @return 是否设置成功
     */
    boolean setUserGroup(MonitorApplicationSetUserGroupDTO parameter);
}
