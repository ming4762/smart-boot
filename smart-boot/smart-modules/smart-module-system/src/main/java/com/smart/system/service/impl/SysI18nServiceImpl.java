package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
import com.smart.system.mapper.SysI18nMapper;
import com.smart.system.model.SysI18nItemPO;
import com.smart.system.model.SysI18nPO;
import com.smart.system.pojo.dbo.I18nCodeValueBO;
import com.smart.system.pojo.vo.SysI18nUserVO;
import com.smart.system.service.SysI18nItemService;
import com.smart.system.service.SysI18nService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
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
public class SysI18nServiceImpl extends BaseServiceImpl<SysI18nMapper, SysI18nPO> implements SysI18nService {

    private final UserSetterService userSetterService;

    private final SysI18nItemService sysI18nItemService;

    public SysI18nServiceImpl(UserSetterService userSetterService, SysI18nItemService sysI18nItemService) {
        this.userSetterService = userSetterService;
        this.sysI18nItemService = sysI18nItemService;
    }

    @Override
    public List<? extends SysI18nPO> list(@NonNull QueryWrapper<SysI18nPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        List<? extends SysI18nPO> i18nList = super.list(queryWrapper, parameter, paging);

        List<SysI18nUserVO> voList = i18nList.stream().map(item -> {
            SysI18nUserVO vo = new SysI18nUserVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.QUERY_CREATE_UPDATE_USER.name()))) {
            this.userSetterService.setCreateUpdateUser(voList);
        }
        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
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
