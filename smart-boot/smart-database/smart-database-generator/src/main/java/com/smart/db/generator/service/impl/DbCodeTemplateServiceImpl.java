package com.smart.db.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.smart.auth.core.constants.RoleEnum;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.utils.BeanUtils;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
import com.smart.db.generator.constants.DbCrudEnum;
import com.smart.db.generator.mapper.DbCodeTemplateMapper;
import com.smart.db.generator.model.DbCodeTemplatePO;
import com.smart.db.generator.model.DbCodeTemplateUserGroupPO;
import com.smart.db.generator.pojo.dto.DbTemplateUserGroupSaveDTO;
import com.smart.db.generator.pojo.vo.template.DbCodeTemplateListVO;
import com.smart.db.generator.service.DbCodeTemplateService;
import com.smart.db.generator.service.DbCodeTemplateUserGroupService;
import com.smart.system.service.SysUserGroupUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/5/7 17:16
 * @since 1.0
 */
@Service
public class DbCodeTemplateServiceImpl extends BaseServiceImpl<DbCodeTemplateMapper, DbCodeTemplatePO> implements DbCodeTemplateService {

    private final DbCodeTemplateUserGroupService dbCodeTemplateUserGroupService;

    private final SysUserGroupUserService sysUserGroupUserService;

    private final UserSetterService userSetterService;

    public DbCodeTemplateServiceImpl(DbCodeTemplateUserGroupService dbCodeTemplateUserGroupService, SysUserGroupUserService sysUserGroupUserService, UserSetterService userSetterService) {
        this.dbCodeTemplateUserGroupService = dbCodeTemplateUserGroupService;
        this.sysUserGroupUserService = sysUserGroupUserService;
        this.userSetterService = userSetterService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<? extends DbCodeTemplatePO> list(@NonNull QueryWrapper<DbCodeTemplatePO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        queryWrapper.select(DbCodeTemplatePO.class, field -> !"template".equals(field.getProperty()));
        // ??????????????????
        // 1?????????????????????????????????
        final RestUserDetails user = AuthUtils.getNonNullCurrentUser();
        if (!user.getRoles().contains(RoleEnum.ROLE_SUPERADMIN.getRole())) {
            // ??????????????????????????????????????????
            // 2??????????????????
            final List<Long> templateIdList = this.listTemplateIdByUserId(user.getUserId());
            // 3????????????
            queryWrapper.lambda().and(query -> {
                query.eq(DbCodeTemplatePO :: getCreateUserId, user.getUserId());
                if (CollectionUtils.isNotEmpty(templateIdList)) {
                    query.or(queryOr -> queryOr.in(DbCodeTemplatePO :: getTemplateId, templateIdList));
                }
            });
        }
        List<? extends DbCodeTemplatePO> list = super.list(queryWrapper, parameter, paging);
        // ?????????VO
        List<DbCodeTemplateListVO> voList = BeanUtils.copyProperties(list, DbCodeTemplateListVO.class);
        // ????????????????????????
        if (Boolean.TRUE.equals(parameter.getParameter().get(DbCrudEnum.QUERY_CREATE_UPDATE_USER.name()))) {
            this.userSetterService.setCreateUpdateUser(voList);
        }
        return voList;
    }

    /**
     * ??????????????????????????????????????????ID
     * @param userId ??????ID
     * @return ??????ID??????
     */
    private List<Long> listTemplateIdByUserId(@NonNull Long userId) {
        // 1??????????????????ID
        final List<Long> groupIdList = this.sysUserGroupUserService.listGroupIdByUserId(Lists.newArrayList(userId)).get(userId);
        if (CollectionUtils.isEmpty(groupIdList)) {
            return Lists.newArrayList();
        }
        // 2??????????????????ID????????????ID
        return this.dbCodeTemplateUserGroupService.list(
                new QueryWrapper<DbCodeTemplateUserGroupPO>().lambda()
                        .select(DbCodeTemplateUserGroupPO :: getTemplateId)
                        .in(DbCodeTemplateUserGroupPO :: getGroupId, groupIdList)
        ).stream().map(DbCodeTemplateUserGroupPO :: getTemplateId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTemplateUserGroup(DbTemplateUserGroupSaveDTO parameter) {
        // ????????????
        this.dbCodeTemplateUserGroupService.remove(
                new QueryWrapper<DbCodeTemplateUserGroupPO>().lambda()
                .eq(DbCodeTemplateUserGroupPO :: getTemplateId, parameter.getTemplateId())
        );
        if (CollectionUtils.isNotEmpty(parameter.getGroupIdList())) {
            final List<DbCodeTemplateUserGroupPO> templateUserGroupList = parameter.getGroupIdList().stream()
                    .map(item -> new DbCodeTemplateUserGroupPO(parameter.getTemplateId(), item))
                    .collect(Collectors.toList());
            this.dbCodeTemplateUserGroupService.saveBatchWithUser(templateUserGroupList, AuthUtils.getCurrentUserId());
        }
        // ????????????
        return true;
    }
}
