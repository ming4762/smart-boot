package com.smart.monitor.server.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.monitor.server.manager.mapper.MonitorUserGroupApplicationMapper;
import com.smart.monitor.server.manager.model.MonitorUserGroupApplicationPO;
import com.smart.monitor.server.manager.pojo.dto.MonitorApplicationSetUserGroupDTO;
import com.smart.monitor.server.manager.service.MonitorUserGroupApplicationService;
import com.smart.system.service.SysUserGroupUserService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2022/2/9
 * @since 2.0.0
 */
@Service
public class MonitorUserGroupApplicationServiceImpl extends BaseServiceImpl<MonitorUserGroupApplicationMapper, MonitorUserGroupApplicationPO> implements MonitorUserGroupApplicationService {

    private final SysUserGroupUserService sysUserGroupUserService;

    public MonitorUserGroupApplicationServiceImpl(SysUserGroupUserService sysUserGroupUserService) {
        this.sysUserGroupUserService = sysUserGroupUserService;
    }

    /**
     * 查询用户关联的应用ID
     * @param userId 用户ID
     * @return 应用ID列表
     */
    @Override
    public List<Long> listApplicationIdByUserId(@NonNull Long userId) {
        // 查询用户所在用户组
        List<Long> userGroupIdList = this.sysUserGroupUserService.listGroupIdByUserId(Lists.newArrayList(userId)).get(userId);
        if (CollectionUtils.isEmpty(userGroupIdList)) {
            return new ArrayList<>(0);
        }
        return this.list(
                new QueryWrapper<MonitorUserGroupApplicationPO>().lambda()
                .select(MonitorUserGroupApplicationPO :: getApplicationId)
                .in(MonitorUserGroupApplicationPO :: getUserGroupId, userGroupIdList)
        ).stream().map(MonitorUserGroupApplicationPO :: getApplicationId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setUserGroup(MonitorApplicationSetUserGroupDTO parameter) {
        // 删除数据
        this.remove(
                new QueryWrapper<MonitorUserGroupApplicationPO>().lambda()
                .eq(MonitorUserGroupApplicationPO :: getApplicationId, parameter.getApplicationId())
        );
        // 保存数据
        if (!CollectionUtils.isEmpty(parameter.getGroupIdList())) {
            List<MonitorUserGroupApplicationPO> data = parameter.getGroupIdList().stream().map(item -> {
                MonitorUserGroupApplicationPO po = new MonitorUserGroupApplicationPO();
                po.setApplicationId(parameter.getApplicationId());
                po.setUserGroupId(item);
                return po;
            }).collect(Collectors.toList());
            this.saveBatchWithUser(data, AuthUtils.getCurrentUserId());
        }
        return true;
    }
}
