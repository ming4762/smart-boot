package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.crud.utils.CrudPageHelper;
import com.smart.module.system.mapper.SysUserGroupMapper;
import com.smart.module.system.model.SysUserGroupPO;
import com.smart.module.system.model.SysUserGroupUserPO;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.pojo.dto.UserGroupUserSaveDTO;
import com.smart.module.system.pojo.dto.UserUserGroupSaveDTO;
import com.smart.module.system.service.SysUserGroupService;
import com.smart.module.system.service.SysUserGroupUserService;
import com.smart.module.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户组服务层
 * @author jackson
 * 2020/1/24 3:05 下午
 */
@Service
@RequiredArgsConstructor
public class SysUserGroupServiceImpl extends BaseServiceImpl<SysUserGroupMapper, SysUserGroupPO> implements SysUserGroupService {

    private final SysUserGroupUserService sysUserGroupUserService;

    private final SysUserService sysUserService;

    /**
     * 重写批量删除
     *
     * @param idList ID列表
     * @return 删除用户组用户关系
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> idList) {
        // 删除用户组用户关系
        if (!CollectionUtils.isEmpty(idList)) {
            this.sysUserGroupUserService.remove(
                    new UpdateWrapper<SysUserGroupUserPO>().lambda().in(SysUserGroupUserPO::getUserGroupId, idList)
            );
        }
        return super.removeByIds(idList);
    }

    /**
     * 查询用户组ID包含的用户id集合
     *
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
                        .in(SysUserGroupUserPO::getUserGroupId, groupIds)
        );
        if (!sysUserGroupUserList.isEmpty()) {
            // 分组转换
            return sysUserGroupUserList.stream()
                    .collect(Collectors.groupingBy(SysUserGroupUserPO::getUserGroupId, Collectors.mapping(SysUserGroupUserPO::getUserId, Collectors.toList())));
        }
        return Maps.newHashMap();
    }

    /**
     * 查询用户组ID包含的用户集合
     *
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
                                    .filter(ObjectUtils::isNotEmpty)
                                    .toList()));
                    return result;
                }
            }
        }
        return Maps.newHashMap();
    }

    /**
     * 保存用户组的用户信息
     *
     * @param parameter 数据
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserGroupByGroupId(@NonNull UserGroupUserSaveDTO parameter) {
        // 删除用户组用户信息信息
        this.sysUserGroupUserService.remove(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                        .eq(SysUserGroupUserPO::getUserGroupId, parameter.getGroupId())
        );
        // 保存用户的用户组信息
        final List<SysUserGroupUserPO> sysUserGroupUserList = parameter.getUserIdList().stream()
                .distinct()
                .map(item -> SysUserGroupUserPO.builder()
                        .userGroupId(parameter.getGroupId())
                        .userId(item)
                        .useYn(Boolean.TRUE)
                        .build()
                ).toList();
        return this.sysUserGroupUserService.saveBatch(sysUserGroupUserList);
    }

    /**
     * 保存用户的用户组信息
     *
     * @param parameter 用户组信息
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserGroupByUserId(@NonNull UserUserGroupSaveDTO parameter) {
        // 删除用户组用户信息信息
        this.sysUserGroupUserService.remove(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                        .eq(SysUserGroupUserPO::getUserId, parameter.getUserId())
        );
        // 保存用户的用户组信息
        final List<SysUserGroupUserPO> sysUserGroupUserList = parameter.getGroupIdList().stream()
                .distinct()
                .map(item -> SysUserGroupUserPO.builder()
                        .userId(parameter.getUserId())
                        .userGroupId(item)
                        .useYn(Boolean.TRUE)
                        .build()
                ).toList();
        return this.sysUserGroupUserService.saveBatch(sysUserGroupUserList);
    }

    /**
     * 查询用户组ID包含的未绑定用户集合
     *
     * @param groupIds 用户组ID
     * @return 用户组ID包含的未绑定用户集合
     */
     @Override
    public @org.jspecify.annotations.NonNull List<SysUserPO> listNoBindUserByIds(@org.jspecify.annotations.NonNull Collection<Long> groupIds,
                                                                                 @Nullable QueryWrapper<SysUserPO> queryWrapper) {
        String inParameter = groupIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        Page<SysUserPO> page = CrudPageHelper.get();
        QueryWrapper<SysUserPO> wrapper = Objects.requireNonNullElseGet(queryWrapper, QueryWrapper::new);
        wrapper.lambda()
                 .eq(SysUserPO::getUseYn, Boolean.TRUE)
                .apply("user_id not in (select A.user_id from sys_user_group_user A where A.user_group_id in ({0}))", inParameter)
                .apply("user_id in (select A.user_id from sys_tenant_user A where A.tenant_id = {0})", AuthUtils.getNonNullCurrentTenantId());
         return this.sysUserService.list(page, wrapper);
    }

    /**
     * 解绑用户组的用户
     *
     * @param groupId    用户组ID
     * @param userIdList 用户ID列表
     * @return 是否解绑成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unBindUser(@org.jspecify.annotations.NonNull Long groupId, @org.jspecify.annotations.NonNull List<Long> userIdList) {
        // 删除用户组用户信息信息
        return this.sysUserGroupUserService.remove(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                        .eq(SysUserGroupUserPO::getUserGroupId, groupId)
                        .in(SysUserGroupUserPO::getUserId, userIdList)
        );
    }
}
