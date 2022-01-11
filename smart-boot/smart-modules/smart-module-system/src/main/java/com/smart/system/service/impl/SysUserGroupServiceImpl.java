package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
import com.smart.system.mapper.SysUserGroupMapper;
import com.smart.system.model.SysUserGroupPO;
import com.smart.system.model.SysUserGroupUserPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.pojo.dto.UserGroupUserSaveDTO;
import com.smart.system.pojo.dto.UserUserGroupSaveDTO;
import com.smart.system.pojo.vo.SysUserGroupListVO;
import com.smart.system.service.SysUserGroupService;
import com.smart.system.service.SysUserGroupUserService;
import com.smart.system.service.SysUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户组服务层
 * @author jackson
 * 2020/1/24 3:05 下午
 */
@Service
public class SysUserGroupServiceImpl extends BaseServiceImpl<SysUserGroupMapper, SysUserGroupPO> implements SysUserGroupService {

    private final SysUserGroupUserService sysUserGroupUserService;

    private final SysUserService sysUserService;

    private final UserSetterService userSetterService;

    public SysUserGroupServiceImpl(SysUserGroupUserService sysUserGroupUserService, SysUserService sysUserService, UserSetterService userSetterService) {
        this.sysUserGroupUserService = sysUserGroupUserService;
        this.sysUserService = sysUserService;
        this.userSetterService = userSetterService;
    }

    @Override
    public List<SysUserGroupPO> list(@NonNull QueryWrapper<SysUserGroupPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        List<SysUserGroupPO> userGroupList = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(userGroupList)) {
            return new ArrayList<>(0);
        }
        List<SysUserGroupListVO> voList = userGroupList.stream()
                .map(item -> {
                    SysUserGroupListVO vo = new SysUserGroupListVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).collect(Collectors.toList());
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.QUERY_CREATE_UPDATE_USER.name()))) {
            this.userSetterService.setCreateUpdateUser(voList);
        }
        return voList.stream().map(SysUserGroupPO.class :: cast).collect(Collectors.toList());
    }

    /**
     * 重写批量删除
     * @param idList ID列表
     * @return 删除用户组用户关系
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        // 删除用户组用户关系
        if (!CollectionUtils.isEmpty(idList)) {
            this.sysUserGroupUserService.remove(
                    new UpdateWrapper<SysUserGroupUserPO>().lambda().in(SysUserGroupUserPO :: getUserGroupId, idList)
            );
        }
        return super.removeByIds(idList);
    }

    /**
     * 查询用户组ID包含的用户id集合
     * @param groupIds 用户组ID
     * @return 用户组ID包含的用户id集合
     */
    @Override
    @NonNull
    public Map<Long, List<Long>> listUserIdByIds(@NonNull Collection<Long> groupIds) {
        if (groupIds.isEmpty()) {
            return Maps.newHashMap();
        }
        // 查询用户组-用户信息
        final List<SysUserGroupUserPO> sysUserGroupUserList = this.sysUserGroupUserService.list(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                .in(SysUserGroupUserPO :: getUserGroupId, groupIds)
        );
        if (!sysUserGroupUserList.isEmpty()) {
            // 分组转换
            return sysUserGroupUserList.stream()
                    .collect(Collectors.groupingBy(SysUserGroupUserPO :: getUserGroupId, Collectors.mapping(SysUserGroupUserPO::getUserId, Collectors.toList())));
        }
        return Maps.newHashMap();
    }

    /**
     * 查询用户组ID包含的用户集合
     * @param groupIds 用户组ID
     * @return 查询结果
     */
    @Override
    @NonNull
    public Map<Long, List<SysUserPO>> listUserByIds(@NonNull Collection<Long> groupIds) {
        // 查询用户ID信息
        final Map<Long, List<Long>> idResult = this.listUserIdByIds(groupIds);
        if (!idResult.isEmpty()) {
            // 获取人员ID
            final Set<Long> userIds = Sets.newHashSet();
            idResult.forEach((key, value) -> userIds.addAll(value));
            if (!userIds.isEmpty()) {
                // 查询人员信息并转为map
                final Map<Long, SysUserPO> userMap = this.sysUserService.listByIds(userIds)
                        .stream()
                        .collect(Collectors.toMap(SysUserPO::getUserId, item -> item));
                if (!userMap.isEmpty()) {
                    final Map<Long, List<SysUserPO>> result = Maps.newHashMap();
                    // 通过idResult， userMap组装结果
                    idResult.forEach((key, value) -> result.put(key,
                            value.stream().
                                    map(userMap::get)
                                    .filter(ObjectUtils :: isNotEmpty)
                                    .collect(Collectors.toList())));
                    return result;
                }
            }
        }
        return Maps.newHashMap();
    }

    /**
     * 保存用户组的用户信息
     * @param parameter 数据
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserGroupByGroupId(@NonNull UserGroupUserSaveDTO parameter) {
        // 删除用户组用户信息信息
        this.sysUserGroupUserService.remove(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                .eq(SysUserGroupUserPO :: getUserGroupId, parameter.getGroupId())
        );
        // 保存用户的用户组信息
        final List<SysUserGroupUserPO> sysUserGroupUserList = parameter.getUserIdList().stream()
                .distinct()
                .map(item -> SysUserGroupUserPO.builder()
                        .userGroupId(parameter.getGroupId())
                        .userId(item)
                        .useYn(Boolean.TRUE)
                        .build()
                ).collect(Collectors.toList());
        return this.sysUserGroupUserService.saveBatch(sysUserGroupUserList);
    }

    /**
     * 保存用户的用户组信息
     * @param parameter 用户组信息
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserGroupByUserId(@NonNull UserUserGroupSaveDTO parameter) {
        // 删除用户组用户信息信息
        this.sysUserGroupUserService.remove(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                    .eq(SysUserGroupUserPO :: getUserId, parameter.getUserId())
        );
        // 保存用户的用户组信息
        final List<SysUserGroupUserPO> sysUserGroupUserList = parameter.getGroupIdList().stream()
                .distinct()
                .map(item -> SysUserGroupUserPO.builder()
                        .userId(parameter.getUserId())
                        .userGroupId(item)
                        .useYn(Boolean.TRUE)
                        .build()
                ).collect(Collectors.toList());
        return this.sysUserGroupUserService.saveBatch(sysUserGroupUserList);
    }
}
