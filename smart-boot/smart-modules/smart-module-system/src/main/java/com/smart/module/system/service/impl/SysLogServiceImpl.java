package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.crud.constants.CrudCommonEnum;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.crud.service.UserSetterService;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.system.constants.SystemConstantEnum;
import com.smart.module.system.mapper.SysLogMapper;
import com.smart.module.system.model.SysLogPO;
import com.smart.module.system.model.tenant.SysTenantPO;
import com.smart.module.system.pojo.vo.SysLogListVO;
import com.smart.module.system.service.SysLogService;
import com.smart.module.system.service.tenant.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0.7
 */
@Service
@RequiredArgsConstructor
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLogPO> implements SysLogService {

    private static final List<String> LIST_NO_SELECT_FIELDS = Lists.newArrayList("params", "errorMessage", "result");

    private final UserSetterService userSetterService;
    private final SysTenantService sysTenantService;

    @Override
    public List<? extends SysLogPO> list(@NonNull QueryWrapper<SysLogPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        queryWrapper.select(SysLogPO.class, field -> !LIST_NO_SELECT_FIELDS.contains(field.getProperty()));
        if (!AuthUtils.isPlatformTenant()) {
            queryWrapper.lambda().eq(SysLogPO::getTenantId, AuthUtils.getNonNullCurrentTenantId());
        }
        List<? extends SysLogPO> sysLogList = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(sysLogList)) {
            return Lists.newArrayList();
        }
        List<SysLogListVO> logVoList = sysLogList.stream().map(item -> {
            SysLogListVO vo = new SysLogListVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).toList();
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.QUERY_CREATE_UPDATE_USER.name()))) {
            this.userSetterService.setCreateUser(logVoList);
        }
        if (Boolean.TRUE.equals(parameter.getParameter().get(SystemConstantEnum.LIST_WITH_TENANT))) {
            this.queryTenant(logVoList);
        }
        return logVoList;
    }

    private void queryTenant(List<SysLogListVO> logVoList) {
        if (CollectionUtils.isEmpty(logVoList)) {
            return;
        }
        Set<Long> tenantIds = logVoList.stream()
                .map(SysLogListVO::getTenantId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(tenantIds)) {
            return;
        }
        Map<Long, SysTenantPO> tenantMap = CrudUtils.partitionList(tenantIds, 900, list -> this.sysTenantService.lambdaQuery()
                .select(SysTenantPO::getId, SysTenantPO::getTenantCode, SysTenantPO::getTenantName, SysTenantPO::getTenantShortName)
                .in(SysTenantPO::getId, list)
                .list()
        ).stream().collect(Collectors.toMap(SysTenantPO::getId, item -> item));
        if (CollectionUtils.isEmpty(tenantMap)) {
            return;
        }
        logVoList.forEach(item -> {
            if (item.getTenantId() != null) {
                item.setTenant(tenantMap.get(item.getTenantId()));
            }
        });
    }
}
