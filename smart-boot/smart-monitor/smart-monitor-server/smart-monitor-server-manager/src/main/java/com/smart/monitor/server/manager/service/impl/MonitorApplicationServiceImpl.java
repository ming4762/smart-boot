package com.smart.monitor.server.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
import com.smart.monitor.server.manager.mapper.MonitorApplicationMapper;
import com.smart.monitor.server.manager.model.MonitorApplicationPO;
import com.smart.monitor.server.manager.pojo.vo.MonitorApplicationUserVO;
import com.smart.monitor.server.manager.service.MonitorApplicationService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2021/3/13 8:48 下午
 */
@Service
public class MonitorApplicationServiceImpl extends BaseServiceImpl<MonitorApplicationMapper, MonitorApplicationPO> implements MonitorApplicationService {

    private final UserSetterService userSetterService;

    public MonitorApplicationServiceImpl(UserSetterService userSetterService) {
        this.userSetterService = userSetterService;
    }

    @NonNull
    @Override
    public List<? extends MonitorApplicationPO> list(@NonNull QueryWrapper<MonitorApplicationPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        Boolean filterUser = (Boolean) parameter.getParameter().get(CrudCommonEnum.FILTER_BY_USER.name());
        if (Boolean.TRUE.equals(filterUser)) {
            // 绑定当前登录用户
//            final Long userId = AuthUtils.getCurrentUserId();
//            if (userId == null) {
//                return Lists.newArrayList();
//            }
//            final List<Long> applicationIdList = this.monitorUserApplicationService.getApplicationIdByUserIds(Lists.newArrayList(userId)).get(userId);
//            if (CollectionUtils.isEmpty(applicationIdList)) {
//                return Lists.newArrayList();
//            }
//            queryWrapper.lambda()
//                    .in(MonitorApplicationPO :: getId, applicationIdList);
        }
        List<? extends MonitorApplicationPO> data = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(data)) {
            return new ArrayList<>(0);
        }
        List<MonitorApplicationUserVO> voList = data.stream().map(item -> {
            MonitorApplicationUserVO vo = new MonitorApplicationUserVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.QUERY_CREATE_UPDATE_USER.name()))) {
            this.userSetterService.setCreateUpdateUser(voList);
        }
        return voList;
    }

    @Override
    public boolean save(@NonNull MonitorApplicationPO entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setCreateUserId(AuthUtils.getCurrentUserId());
        entity.setUseYn(true);
        entity.setDeleteYn(false);
        return super.save(entity);
    }

    @Override
    public boolean updateById(MonitorApplicationPO entity) {
        entity.setUpdateTime(LocalDateTime.now());
        entity.setUpdateUserId(AuthUtils.getCurrentUserId());
        entity.setCreateUserId(null);
        entity.setCreateTime(null);
        entity.setUseYn(null);
        entity.setDeleteYn(null);
        return super.updateById(entity);
    }
}
