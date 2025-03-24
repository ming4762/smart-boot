package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.system.mapper.SysExceptionMapper;
import com.smart.module.system.model.SysExceptionPO;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.model.tenant.SysTenantPO;
import com.smart.module.system.pojo.dto.exception.ExceptionFeedbackDTO;
import com.smart.module.system.pojo.dto.exception.SysExceptionMarkResolvedParameter;
import com.smart.module.system.pojo.vo.SysExceptionListVO;
import com.smart.module.system.service.SysExceptionService;
import com.smart.module.system.service.SysUserService;
import com.smart.module.system.service.tenant.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
* sys_exception - 系统异常信息 Service实现类
* @author GCCodeGenerator
* 2022年6月10日
*/
@Service
@RequiredArgsConstructor
public class SysExceptionServiceImpl extends BaseServiceImpl<SysExceptionMapper, SysExceptionPO> implements SysExceptionService {

    private final SysUserService sysUserService;
    private final SysTenantService sysTenantService;

    @Override
    public List<? extends SysExceptionPO> list(@NonNull QueryWrapper<SysExceptionPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        queryWrapper.select(SysExceptionPO.class, field -> !"stackTrace".equals(field.getProperty()));
        List<? extends SysExceptionPO> list = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<SysExceptionListVO> voList = list.stream()
                .map(item -> {
                    SysExceptionListVO vo = new SysExceptionListVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).toList();
        // 查询结果
        this.queryResolvedUser(voList);
        // 查询租户信息
        this.queryTenant(voList);
        return voList;
    }

    private void queryResolvedUser(List<SysExceptionListVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }
        Set<Long> userIds = voList.stream()
                .map(SysExceptionPO::getResolvedUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        Map<Long, SysUserPO> sysUserMap = CrudUtils.partitionList(userIds, 900, this.sysUserService::listByIds)
                .stream()
                .collect(Collectors.toMap(SysUserPO::getUserId, item -> item));
        if (CollectionUtils.isEmpty(sysUserMap)) {
            return;
        }
        voList.forEach(item -> item.setResolvedUser(sysUserMap.get(item.getResolvedUserId())));
    }

    /**
     * 查询租户信息
     * @param voList vo list
     */
    private void queryTenant(List<SysExceptionListVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }
        Set<Long> tenantIds = voList.stream()
                .map(SysExceptionPO::getTenantId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(tenantIds)) {
            return;
        }
        Map<Long, SysTenantPO> tenantMap = CrudUtils.partitionList(new ArrayList<>(tenantIds), 900,
                        list -> this.sysTenantService.lambdaQuery()
                                .select(SysTenantPO::getId, SysTenantPO::getTenantCode, SysTenantPO::getTenantName, SysTenantPO::getTenantShortName)
                                .in(SysTenantPO::getId, list)
                                .list()
                        )
                .stream()
                .collect(Collectors.toMap(SysTenantPO::getId, item -> item));
        if (CollectionUtils.isEmpty(tenantMap)) {
            return;
        }
        voList.forEach(item -> item.setTenant(tenantMap.get(item.getTenantId())));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean feedback(ExceptionFeedbackDTO parameter) {
        if (CollectionUtils.isEmpty(parameter.getIdList())) {
            return false;
        }
        return this.update(
                new UpdateWrapper<SysExceptionPO>().lambda()
                        .set(SysExceptionPO::getFeedbackMessage, parameter.getFeedbackMessage())
                        .set(SysExceptionPO::getUserFeedback, true)
                        .set(SysExceptionPO::getFeedbackTime, ZonedDateTime.now())
                        .in(SysExceptionPO::getId, parameter.getIdList())
        );
    }

    /**
     * 标记异常已解决
     *
     * @param parameter 参数
     * @return 是否操作成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markResolved(SysExceptionMarkResolvedParameter parameter) {
        if (CollectionUtils.isEmpty(parameter.getExceptionIdList())) {
            return false;
        }
        return this.update(
                new UpdateWrapper<SysExceptionPO>().lambda()
                        .set(SysExceptionPO::getResolved, Boolean.TRUE)
                        .set(SysExceptionPO::getResolvedTime, ZonedDateTime.now())
                        .set(SysExceptionPO::getResolvedMessage, parameter.getResolvedMessage())
                        .set(SysExceptionPO::getResolvedUserId, AuthUtils.getNonNullCurrentUserId())
                        .in(SysExceptionPO::getId, parameter.getExceptionIdList())
        );
    }
}
