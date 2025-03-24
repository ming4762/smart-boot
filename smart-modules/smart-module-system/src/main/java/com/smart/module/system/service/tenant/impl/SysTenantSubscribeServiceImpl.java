package com.smart.module.system.service.tenant.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.constants.SystemConstantEnum;
import com.smart.module.system.mapper.tenant.SysTenantSubscribeMapper;
import com.smart.module.system.model.tenant.SysTenantPackagePO;
import com.smart.module.system.model.tenant.SysTenantSubscribePO;
import com.smart.module.system.pojo.dto.tenant.SysTenantSubscribeListDTO;
import com.smart.module.system.pojo.vo.tenant.SysTenantSubscribeListVO;
import com.smart.module.system.service.tenant.SysTenantPackageService;
import com.smart.module.system.service.tenant.SysTenantSubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* sys_tenant_subscribe - 租户套餐订阅表 Service实现类
* @author SmartCodeGenerator
* 2024年4月6日 下午6:41:30
*/
@Service
@RequiredArgsConstructor
public class SysTenantSubscribeServiceImpl extends BaseServiceImpl<SysTenantSubscribeMapper, SysTenantSubscribePO> implements SysTenantSubscribeService {

    private final SysTenantPackageService sysTenantPackageService;

    /**
     * 查询函数
     *
     * @param queryWrapper 查询参数
     * @param parameter    原始参数
     * @param paging       是否分页
     * @return 查询结果
     */
    @Override
    public List<? extends SysTenantSubscribePO> list(@NonNull QueryWrapper<SysTenantSubscribePO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        SysTenantSubscribeListDTO dto = (SysTenantSubscribeListDTO) parameter;
        if (dto.getTenantId() != null) {
            queryWrapper.lambda()
                    .eq(SysTenantSubscribePO::getTenantId, dto.getTenantId());
        }
        List<? extends SysTenantSubscribePO> list = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<SysTenantSubscribeListVO> voList = list.stream().map(item -> {
            SysTenantSubscribeListVO vo = new SysTenantSubscribeListVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).toList();
        if (Boolean.TRUE.equals(parameter.getParameter().get(SystemConstantEnum.TENANT_SUBSCRIBE_LIST_WITH_PACKAGE.name()))) {
            this.queryPackage(voList);
        }
        return voList;
    }

    /**
     * 查询套餐信息
     * @param voList vo list
     */
    private void queryPackage(List<SysTenantSubscribeListVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }
        Set<Long> packageIds = voList.stream().map(SysTenantSubscribePO::getPackageId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(packageIds)) {
            return;
        }
        Map<Long, SysTenantPackagePO> packageMap = this.sysTenantPackageService.listByIds(packageIds).stream()
                .collect(Collectors.toMap(SysTenantPackagePO::getId, item -> item));
        if (CollectionUtils.isEmpty(packageMap)) {
            return;
        }
        voList.forEach(item -> item.setTenantPackage(packageMap.get(item.getPackageId())));
    }
}