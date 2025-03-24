package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.constants.SystemConstantEnum;
import com.smart.module.system.mapper.SysDictMapper;
import com.smart.module.system.model.SysDictItemPO;
import com.smart.module.system.model.SysDictPO;
import com.smart.module.system.model.tenant.SysTenantPO;
import com.smart.module.system.pojo.vo.SysDictVO;
import com.smart.module.system.service.SysDictItemService;
import com.smart.module.system.service.SysDictService;
import com.smart.module.system.service.tenant.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* sys_dict - 系统字典表 Service实现类
* @author GCCodeGenerator
* 2022-1-29 10:34:36
*/
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends BaseServiceImpl<SysDictMapper, SysDictPO> implements SysDictService {

    private final SysDictItemService sysDictItemService;
    private final SysTenantService sysTenantService;

    /**
     * 查询函数
     *
     * @param queryWrapper 查询参数
     * @param parameter    原始参数
     * @param paging       是否分页
     * @return 查询结果
     */
    @Override
    public List<? extends SysDictPO> list(@NonNull QueryWrapper<SysDictPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        if (Boolean.TRUE.equals(parameter.getParameter().get(SystemConstantEnum.LIST_FILTER_TENANT))) {
            queryWrapper.lambda()
                    .and(query -> {
                        query.eq(SysDictPO::getTenantId, AuthUtils.getNonNullCurrentTenantId());
                        if (AuthUtils.isPlatformTenant()) {
                            query.or(wrapper -> wrapper.eq(SysDictPO::getTenantCommonYn, Boolean.TRUE));
                        }
                    });
        }
        List<? extends SysDictPO> dataList = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(dataList)) {
            return dataList;
        }
        List<SysDictVO> voList = dataList.stream()
                .map(item -> {
                    SysDictVO vo = new SysDictVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).toList();
        if (Boolean.TRUE.equals(parameter.getParameter().get(SystemConstantEnum.LIST_WITH_TENANT))) {
            this.queryTenant(voList);
        }

        return voList;
    }

    /**
     * 查询租户信息
     * @param voList voList
     */
    private void queryTenant(List<SysDictVO> voList) {
        Set<Long> tenantIds = voList.stream()
                .map(SysDictPO::getTenantId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(tenantIds)) {
            return;
        }
        Map<Long, SysTenantPO> tenantMap = this.sysTenantService.lambdaQuery()
                .select(SysTenantPO::getId, SysTenantPO::getTenantName, SysTenantPO::getTenantCode, SysTenantPO::getTenantShortName)
                .in(SysTenantPO::getId, tenantIds)
                .list().stream()
                .collect(Collectors.toMap(SysTenantPO::getId, item -> item));
        voList.forEach(item -> item.setTenant(tenantMap.get(item.getTenantId())));
    }

    /**
     * 重写删除函数
     * 删除字典的同时也删除字典项
     * @param idList ID列表
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        // 删除字典项
        Lists.partition(new ArrayList<>(idList), 200).forEach(list -> this.sysDictItemService.remove(
                new QueryWrapper<SysDictItemPO>().lambda()
                        .in(SysDictItemPO::getDictId, list)
        ));
        return super.removeByIds(idList);
    }

    /**
     * 通过code查询item
     *
     * @param dictCode dict code
     * @return dict item list
     */
    @Override
    public List<SysDictItemPO> listItemByCode(String dictCode) {
        return Optional.ofNullable(this.listItemByCode(List.of(dictCode)).get(dictCode))
                .orElse(new ArrayList<>(0));
    }

    /**
     * 通过code批量查询item
     *
     * @param dictCodeList 字典编码列表
     * @return 字典编码为key，字典项为value的list
     */
    @Override
    public Map<String, List<SysDictItemPO>> listItemByCode(List<String> dictCodeList) {
        if (CollectionUtils.isEmpty(dictCodeList)) {
            return Collections.emptyMap();
        }
        List<SysDictPO> dictList = this.list(
                new QueryWrapper<SysDictPO>().lambda()
                        .select(SysDictPO::getId, SysDictPO::getDictCode)
                        .in(SysDictPO::getDictCode, dictCodeList)
                        .eq(SysDictPO::getUseYn, Boolean.TRUE)
        );
        if (CollectionUtils.isEmpty(dictList)) {
            return Collections.emptyMap();
        }
        Map<Long, String> dictIdCodeMap = dictList.stream()
                .collect(Collectors.toMap(SysDictPO::getId, SysDictPO::getDictCode));
        return this.sysDictItemService.list(
                new QueryWrapper<SysDictItemPO>().lambda()
                        .in(SysDictItemPO::getDictId, dictIdCodeMap.keySet())
                        .eq(SysDictItemPO::getUseYn, Boolean.TRUE)
                        .orderByAsc(SysDictItemPO::getSeq)
        ).stream()
                .collect(Collectors.groupingBy(item -> dictIdCodeMap.get(item.getDictId())));
    }
}
