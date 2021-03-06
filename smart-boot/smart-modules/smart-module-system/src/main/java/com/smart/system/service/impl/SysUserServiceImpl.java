package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.utils.DigestUtils;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.constants.UserPropertyEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
import com.smart.system.constants.FunctionTypeEnum;
import com.smart.system.mapper.SysUserGroupRoleMapper;
import com.smart.system.mapper.SysUserGroupUserMapper;
import com.smart.system.mapper.SysUserMapper;
import com.smart.system.model.*;
import com.smart.system.pojo.dbo.SysUserWthAccountBO;
import com.smart.system.pojo.dto.user.UserSetRoleDTO;
import com.smart.system.pojo.vo.SysFunctionListVO;
import com.smart.system.pojo.vo.SysUserListVO;
import com.smart.system.service.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020/1/23 7:44 下午
 */
@Service
@Slf4j
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUserPO> implements SysUserService {

    /**
     * 密码加密盐值
     */
    private static final String SALT = "888888$#@";

    /**
     * 默认密码
     */
    private static final String DEFAULT_PASSWORD = "123456";

    private static final String I18N_LEFT = "{";

    private static final String I18N_RIGHT = "}";

    private final SysUserRoleService sysUserRoleService;

    private final SysUserGroupUserMapper sysUserGroupUserMapper;

    private final SysUserGroupRoleMapper sysUserGroupRoleMapper;

    private SysRoleService sysRoleService;

    private final SysRoleFunctionService sysRoleFunctionService;

    private SysFunctionService sysFunctionService;

    private final UserSetterService userSetterService;

    private final SysUserAccountService sysUserAccountService;

    public SysUserServiceImpl(SysUserRoleService sysUserRoleService, SysUserGroupUserMapper sysUserGroupUserMapper, SysUserGroupRoleMapper sysUserGroupRoleMapper, SysRoleFunctionService sysRoleFunctionService, UserSetterService userSetterService, SysUserAccountService sysUserAccountService) {
        this.sysUserRoleService = sysUserRoleService;
        this.sysUserGroupUserMapper = sysUserGroupUserMapper;
        this.sysUserGroupRoleMapper = sysUserGroupRoleMapper;
        this.sysRoleFunctionService = sysRoleFunctionService;
        this.userSetterService = userSetterService;
        this.sysUserAccountService = sysUserAccountService;
    }

    @Override
    public List<? extends SysUserPO> list(@NonNull QueryWrapper<SysUserPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        List<? extends SysUserPO> userList = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>(0);
        }
        List<SysUserListVO> voList = userList.stream()
                .map(item -> {
                    SysUserListVO vo = new SysUserListVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).collect(Collectors.toList());
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.QUERY_CREATE_UPDATE_USER.name()))) {
            this.userSetterService.setCreateUpdateUser(voList);
        }
        // 查询账户信息
        this.queryUserAccount(voList);
        return voList;
    }

    /**
     * 查询用户账户信息
     * @param userList 用户列表
     */
    private void queryUserAccount(List<SysUserListVO> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        List<Long> userIdList = userList.stream().map(SysUserListVO::getUserId).collect(Collectors.toList());
        Map<Long, SysUserAccountPO> sysUserAccountMap = Lists.partition(userIdList, 400).stream()
                .flatMap(idList -> this.sysUserAccountService.list(
                        new QueryWrapper<SysUserAccountPO>().lambda()
                        .in(SysUserAccountPO::getUserId, idList)
                ).stream()).collect(Collectors.toMap(SysUserAccountPO::getUserId, item -> item));
        userList.forEach(item -> item.setUserAccount(sysUserAccountMap.get(item.getUserId())));
    }

    /**
     * 查询用户的角色列表
     * @param userId 用户Id
     * @return 角色列表
     */
    @Override
    @Transactional(readOnly = true)
    public @NonNull
    List<SysRolePO> listRole(@NonNull Long userId) {
        final Set<Long> roleIdSet = Sets.newHashSet();
        // 1、查询用户对应的角色
        final Set<Long> userRoleIdSet = this.sysUserRoleService.list(
                new QueryWrapper<SysUserRolePO>().lambda()
                        .eq(SysUserRolePO :: getUserId, userId)
                        .eq(SysUserRolePO :: getEnable, Boolean.TRUE)
        ).stream().map(SysUserRolePO :: getRoleId).collect(Collectors.toSet());
        roleIdSet.addAll(userRoleIdSet);
        // 2、查询用户组的角色
        // 查询用户组
        final Set<Long> userGroupIdSet = this.sysUserGroupUserMapper.selectList(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                    .eq(SysUserGroupUserPO :: getUserId, userId)
                    .eq(SysUserGroupUserPO :: getUseYn, Boolean.TRUE)
        ).stream().map(SysUserGroupUserPO :: getUserGroupId).collect(Collectors.toSet());
        // 通过用户组查询角色ID
        if (!userGroupIdSet.isEmpty()) {
            final Set<Long> groupRoleIdSet = this.sysUserGroupRoleMapper.selectList(
                    new QueryWrapper<SysUserGroupRolePO>().lambda()
                    .in(SysUserGroupRolePO :: getGroupId, userGroupIdSet)
                    .eq(SysUserGroupRolePO :: getUseYn, Boolean.TRUE)
            ).stream().map(SysUserGroupRolePO :: getRoleId).collect(Collectors.toSet());
            roleIdSet.addAll(groupRoleIdSet);
        }
        if (roleIdSet.isEmpty()) {
            return Lists.newArrayList();
        }
        return this.sysRoleService.list(
                new QueryWrapper<SysRolePO>().lambda()
                .in(SysRolePO :: getRoleId, roleIdSet)
                .eq(SysRolePO :: getUseYn, Boolean.TRUE)
        );
    }

    /**
     * 重写删除方法：删除用户关系
     * @param idList ID列表
     * @return 是否删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        Lists.partition(Arrays.asList(idList.toArray()), 400).forEach(list -> {
            // 删除用户与用户组管理
            this.sysUserGroupUserMapper.delete(
                    new QueryWrapper<SysUserGroupUserPO>().lambda()
                            .in(SysUserGroupUserPO :: getUserId, list)
            );
            // 删除用户与角色关系
            this.sysUserRoleService.remove(
                    new QueryWrapper<SysUserRolePO>().lambda()
                            .in(SysUserRolePO::getUserId, list)
            );
            this.update(
                    new UpdateWrapper<SysUserPO>().lambda()
                            .set(SysUserPO :: getDeleteYn, true)
                            .in(SysUserPO :: getUserId, list)
            );
        });
        return true;
    }

    /**
     * 重新保存方法设置密码
     * @param entity 实体类
     * @return 是否保存成功
     */
    @Override
    public boolean save(@NonNull SysUserPO entity) {
        entity.setPassword(DigestUtils.sha256(entity.getUsername() + DEFAULT_PASSWORD + SALT, 2));
        return super.save(entity);
    }

    /**
     * 设置人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    @Override
    public <T> void setWithUser(@NonNull List<T> resource) {
        this.setWithUser(resource, true, true);
    }

    /**
     * 设置创建人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    @Override
    public <T> void setWithCreateUser(@NonNull List<T> resource) {
        this.setWithUser(resource, true, false);
    }

    /**
     * 设置更新人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    @Override
    public <T> void setWithUpdateUser(@NonNull List<T> resource) {
        this.setWithUser(resource, false, true);
    }

    /**
     * 设置人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    @SneakyThrows
    private <T> void setWithUser(@NonNull List<T> resource, boolean withCreateUser, boolean withUpdateUser) {
        if (resource.isEmpty()) {
            return;
        }
        Set<Long> userIdSet = Sets.newHashSet();
        for (T item : resource) {
            if (withCreateUser) {
                // 获取创建人员ID
                userIdSet.add((Long) PropertyUtils.getProperty(item, UserPropertyEnum.CREATE_USER_ID.getName()));
            }
            if (withUpdateUser) {
                // 获取创建人员ID
                userIdSet.add((Long) PropertyUtils.getProperty(item, UserPropertyEnum.UPDATE_USER_ID.getName()));
            }
        }
        userIdSet = userIdSet.stream().filter(item -> !Objects.isNull(item)).collect(Collectors.toSet());
        if (userIdSet.isEmpty()) {
            return;
        }
        // 查询人员信息
        Map<Long, SysUserPO> userMap = this.listByIds(userIdSet).stream()
                .collect(Collectors.toMap(SysUserPO :: getUserId, item -> item));
        for (T item : resource) {
            if (withCreateUser) {
                Long createUserId = (Long) PropertyUtils.getProperty(item, UserPropertyEnum.CREATE_USER_ID.getName());
                PropertyUtils.setProperty(item, UserPropertyEnum.CREATE_USER.getName(), userMap.get(createUserId));
            }
            if (withUpdateUser) {
                Long updateUserId = (Long) PropertyUtils.getProperty(item, UserPropertyEnum.UPDATE_USER_ID.getName());
                PropertyUtils.setProperty(item, UserPropertyEnum.UPDATE_USER.getName(), userMap.get(updateUserId));
            }
        }
    }

    /**
     * 查询用户菜单信息
     * @return 菜单列表
     */
    @NonNull
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<SysFunctionListVO> listCurrentUserMenu(List<Locale> localeList) {
        // 获取当前用户的角色信息
        final RestUserDetails userDetails = AuthUtils.getCurrentUser();
        if (Objects.isNull(userDetails)) {
            return Lists.newArrayList();
        }
        return this.listUserFunctionWithLocale(userDetails.getUserId(), ImmutableList.of(FunctionTypeEnum.CATALOG, FunctionTypeEnum.MENU), localeList);
    }

    /**
     * 查询用户功能
     * @param userId 用户ID
     * @param types 查询的功能类型
     * @return 用户ID表
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<SysFunctionPO> listUserFunction(@NonNull Long userId, @NonNull List<FunctionTypeEnum> types) {
        // 查询角色信息
        final Set<Long> roleIds = this.listRole(userId)
                .stream().map(SysRolePO::getRoleId)
                .collect(Collectors.toSet());
        if (roleIds.isEmpty()) {
            return Lists.newArrayList();
        }
        // 查询功能ID
        final Set<Long> functionIds = this.sysRoleFunctionService.list(
                new QueryWrapper<SysRoleFunctionPO>().lambda()
                        .select(SysRoleFunctionPO :: getFunctionId)
                        .in(SysRoleFunctionPO :: getRoleId, roleIds)
        ).stream().map(SysRoleFunctionPO :: getFunctionId).collect(Collectors.toSet());
        if (functionIds.isEmpty()) {
            return Lists.newArrayList();
        }
        // 查询功能信息
        final LambdaQueryWrapper<SysFunctionPO> queryWrapper = new QueryWrapper<SysFunctionPO>().lambda()
                .in(SysFunctionPO :: getFunctionId, functionIds)
                .orderByAsc(SysFunctionPO :: getSeq);
        if (CollectionUtils.isNotEmpty(types)) {
            queryWrapper.in(SysFunctionPO :: getFunctionType, types.stream().map(FunctionTypeEnum::getValue).collect(Collectors.toList()));
        }
        return this.sysFunctionService.list(queryWrapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<SysFunctionListVO> listUserFunctionWithLocale(@NonNull Long userId, List<FunctionTypeEnum> types, List<Locale> localeList) {
        List<SysFunctionPO> functionList = this.listUserFunction(userId, types);
        if (CollectionUtils.isEmpty(functionList)) {
            return new ArrayList<>(0);
        }
        return functionList.stream().map(item -> {
            SysFunctionListVO vo = new SysFunctionListVO();
            BeanUtils.copyProperties(item, vo);
            // 获取国际化信息
            if (CollectionUtils.isNotEmpty(localeList) && StringUtils.isNotBlank(item.getI18nCode()) && item.getI18nCode().startsWith(I18N_LEFT) && item.getI18nCode().endsWith(I18N_RIGHT)) {
                String i18nCode = item.getI18nCode().replace(I18N_LEFT, "").replace(I18N_RIGHT, "");
                vo.setLocales(
                        localeList.stream().collect(Collectors.toMap(Locale::toLanguageTag, locale -> {
                            try {
                                return I18nUtils.get(i18nCode, locale);
                            } catch (NoSuchMessageException e) {
                                log.warn("no such message, code: {}, locale: {}", i18nCode, locale);
                                return "";
                            }
                        }))
                );
            }
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 设置角色
     * @param parameter 参数
     * @return 是否这是成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setRole(@NonNull UserSetRoleDTO parameter) {
        // 删除用户角色关系
        this.sysUserRoleService.remove(
                new QueryWrapper<SysUserRolePO>().lambda().eq(SysUserRolePO :: getUserId, parameter.getUserId())
        );
        // 保存用户角色关系
        if (CollectionUtils.isNotEmpty(parameter.getRoleIdList())) {
            this.sysUserRoleService.saveBatchWithUser(
                    parameter.getRoleIdList().stream().map(roleId -> SysUserRolePO.builder()
                            .roleId(roleId)
                            .enable(Boolean.TRUE)
                            .userId(parameter.getUserId())
                            .build()).collect(Collectors.toList()),
                    AuthUtils.getCurrentUserId()
            );
        }
        return true;
    }

    /**
     * 通过角色ID查询用户信息
     * @param roleIdList 角色ID列表
     * @return 用户信息
     */
    @Override
    public List<SysUserPO> listUserByRoleId(List<Long> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        final Set<Long> userIds = this.sysUserRoleService.listByRoleIdList(roleIdList).stream().map(SysUserRolePO::getUserId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(userIds)) {
            return Lists.newArrayList();
        }
        return this.list(
                new QueryWrapper<SysUserPO>().lambda()
                .in(SysUserPO :: getUserId, userIds)
                .eq(SysUserPO :: getUseYn, Boolean.TRUE)
                .orderByAsc(SysUserPO :: getSeq)
        );
    }

    @Autowired
    public void setSysFunctionService(SysFunctionService sysFunctionService) {
        this.sysFunctionService = sysFunctionService;
    }

    @Autowired
    public void setSysRoleService(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }


    @Override
    public List<SysUserWthAccountBO> listUserWithAccount(QueryWrapper<SysUserPO> parameter) {
        return this.baseMapper.listUserWithAccount(parameter);
    }

}
