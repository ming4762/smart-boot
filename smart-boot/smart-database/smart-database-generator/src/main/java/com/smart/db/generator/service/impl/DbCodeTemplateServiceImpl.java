package com.smart.db.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.utils.BeanUtils;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.db.generator.mapper.DbCodeTemplateMapper;
import com.smart.db.generator.model.DbCodeTemplatePO;
import com.smart.db.generator.model.DbCodeTemplateUserGroupPO;
import com.smart.db.generator.pojo.dto.DbTemplateUserGroupSaveDTO;
import com.smart.db.generator.pojo.vo.template.DbCodeTemplateListVO;
import com.smart.db.generator.service.DbCodeTemplateService;
import com.smart.db.generator.service.DbCodeTemplateUserGroupService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/5/7 17:16
 * @since 1.0
 */
@Service
public class DbCodeTemplateServiceImpl extends BaseServiceImpl<DbCodeTemplateMapper, DbCodeTemplatePO> implements DbCodeTemplateService {

    private final DbCodeTemplateUserGroupService dbCodeTemplateUserGroupService;

    public DbCodeTemplateServiceImpl(DbCodeTemplateUserGroupService dbCodeTemplateUserGroupService) {
        this.dbCodeTemplateUserGroupService = dbCodeTemplateUserGroupService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<? extends DbCodeTemplatePO> list(@NonNull QueryWrapper<DbCodeTemplatePO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        queryWrapper.select(DbCodeTemplatePO.class, field -> !"template".equals(field.getProperty()));
        // 设置查询权限
        // 1、判断是否是超级管理员
        List<? extends DbCodeTemplatePO> list = super.list(queryWrapper, parameter, paging);
        // 转换为VO
        return BeanUtils.copyProperties(list, DbCodeTemplateListVO.class);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTemplateUserGroup(DbTemplateUserGroupSaveDTO parameter) {
        // 删除数据
        this.dbCodeTemplateUserGroupService.remove(
                new QueryWrapper<DbCodeTemplateUserGroupPO>().lambda()
                .eq(DbCodeTemplateUserGroupPO :: getTemplateId, parameter.getTemplateId())
        );
        if (!CollectionUtils.isEmpty(parameter.getGroupIdList())) {
            final List<DbCodeTemplateUserGroupPO> templateUserGroupList = parameter.getGroupIdList().stream()
                    .map(item -> new DbCodeTemplateUserGroupPO(parameter.getTemplateId(), item))
                    .toList();
            this.dbCodeTemplateUserGroupService.saveBatch(templateUserGroupList);
        }
        // 保存数据
        return true;
    }
}
