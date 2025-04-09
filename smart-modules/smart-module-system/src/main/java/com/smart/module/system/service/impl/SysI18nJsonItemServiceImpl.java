package com.smart.module.system.service.impl;

import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.i18n.source.ReloadableMessageSource;
import com.smart.module.system.constants.SysI18nPlatformEnum;
import com.smart.module.system.mapper.SysI18nJsonItemMapper;
import com.smart.module.system.model.SysI18nJsonItemPO;
import com.smart.module.system.model.SysI18nJsonPO;
import com.smart.module.system.service.SysI18nJsonItemService;
import com.smart.module.system.service.SysI18nJsonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* sys_i18n_json_item - 国际化信息json项 Service实现类
* @author SmartCodeGenerator
* 2025年1月3日 19:22:50
*/
@Service
@RequiredArgsConstructor
public class SysI18nJsonItemServiceImpl extends BaseServiceImpl<SysI18nJsonItemMapper, SysI18nJsonItemPO> implements SysI18nJsonItemService {

    private final ObjectProvider<ReloadableMessageSource> reloadableMessageSourceObjectProvider;
    private final ObjectProvider<SysI18nJsonService> sysI18nJsonServices;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(Collection<SysI18nJsonItemPO> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        boolean result = super.saveOrUpdateBatch(entityList);
        // 刷新缓存
        Set<Long> headIds = entityList.stream()
                .map(SysI18nJsonItemPO::getHeadId)
                .collect(Collectors.toSet());
        this.reloadI18nCache(new ArrayList<>(headIds));
        return result;
    }

    /**
     * 刷新国际化缓存
     * @param headIds 国际化Head id
     */
    private void reloadI18nCache(List<Long> headIds) {
        ReloadableMessageSource reloadableMessageSource = reloadableMessageSourceObjectProvider.getIfAvailable();
        if (reloadableMessageSource == null) {
            return;
        }
        boolean hasBackendI18n = this.sysI18nJsonServices.getObject().lambdaQuery()
                .select(SysI18nJsonPO::getId, SysI18nJsonPO::getPlatform)
                .in(SysI18nJsonPO::getId, headIds)
                .list().stream()
                .anyMatch(item -> SysI18nPlatformEnum.BACKEND.equals(item.getPlatform()));
        if (hasBackendI18n) {
            // 更改了后台国际化，重新刷新国际化缓存
            reloadableMessageSource.reload();
        }
    }
}