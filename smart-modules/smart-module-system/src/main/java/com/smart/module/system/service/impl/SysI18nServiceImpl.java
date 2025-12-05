package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.mapper.SysI18nMapper;
import com.smart.module.system.model.SysI18nItemPO;
import com.smart.module.system.model.SysI18nPO;
import com.smart.module.system.pojo.dbo.I18nCodeValueBO;
import com.smart.module.system.service.SysI18nItemService;
import com.smart.module.system.service.SysI18nService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/11/12
 * @since 1.0.7
 */
@Service
@RequiredArgsConstructor
public class SysI18nServiceImpl extends BaseServiceImpl<SysI18nMapper, SysI18nPO> implements SysI18nService {

    private final SysI18nItemService sysI18nItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return true;
        }
        // 删除item
        this.sysI18nItemService.remove(
                new QueryWrapper<SysI18nItemPO>().lambda()
                .in(SysI18nItemPO :: getI18nId, idList)
        );
        return super.removeByIds(idList);
    }

    @Override
    @NonNull
    public Map<String, String> readByLocale(@NonNull Locale locale) {
        return this.baseMapper.listI18nByLocale(locale.toLanguageTag())
                .stream().collect(Collectors.toMap(I18nCodeValueBO::getI18nCode, I18nCodeValueBO::getValue));
    }

    public List<Locale> listLocale() {
        return this.baseMapper.listLocale();
    }
}
