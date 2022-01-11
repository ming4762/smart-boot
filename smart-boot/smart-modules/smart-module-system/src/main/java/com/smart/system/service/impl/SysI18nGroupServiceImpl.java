package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.SysI18nGroupMapper;
import com.smart.system.model.SysI18nGroupPO;
import com.smart.system.model.SysI18nPO;
import com.smart.system.service.SysI18nGroupService;
import com.smart.system.service.SysI18nService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/11/12
 * @since 1.0.7
 */
@Service
public class SysI18nGroupServiceImpl extends BaseServiceImpl<SysI18nGroupMapper, SysI18nGroupPO> implements SysI18nGroupService {

    private final SysI18nService sysI18nService;

    public SysI18nGroupServiceImpl(SysI18nService sysI18nService) {
        this.sysI18nService = sysI18nService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return true;
        }
        // 获取I18N信息
        Set<Long> i18nIds = this.sysI18nService.list(
                new QueryWrapper<SysI18nPO>().lambda()
                .in(SysI18nPO :: getGroupId, idList)
        ).stream().map(SysI18nPO :: getI18nId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(i18nIds)) {
            this.sysI18nService.removeByIds(i18nIds);
        }
        return super.removeByIds(idList);
    }
}
