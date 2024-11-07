package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.smart.framework.auth.core.userdetails.RestUserDetails;
import com.smart.framework.auth.core.utils.AuthUtils;
import com.smart.framework.commons.core.dto.auth.*;
import com.smart.framework.commons.core.exception.BusinessException;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.commons.core.i18n.I18nUtils;
import com.smart.framework.commons.core.tenant.SmartTenantHolder;
import com.smart.framework.commons.core.utils.DigestUtils;
import com.smart.framework.commons.core.utils.PropertyUtils;
import com.smart.framework.commons.core.utils.SmartIdGenerator;
import com.smart.framework.crud.constants.CrudCommonEnum;
import com.smart.framework.crud.constants.ModelPropertyEnum;
import com.smart.framework.crud.datapermission.DataPermissionScope;
import com.smart.framework.crud.parameter.SetUseYnParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.crud.service.UserSetterService;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.api.system.SysParameterApi;
import com.smart.module.api.system.constants.SysParameterCodeEnum;
import com.smart.module.api.system.dto.QueryUserAccountDTO;
import com.smart.module.system.constants.FunctionTypeEnum;
import com.smart.module.system.constants.UserDeptIdentEnum;
import com.smart.module.system.mapper.SysUserGroupUserMapper;
import com.smart.module.system.mapper.SysUserMapper;
import com.smart.module.system.mapper.tenant.SysTenantMapper;
import com.smart.module.system.model.*;
import com.smart.module.system.model.tenant.SysTenantPO;
import com.smart.module.system.model.tenant.SysTenantUserPO;
import com.smart.module.system.pojo.dbo.SysUserWthAccountBO;
import com.smart.module.system.pojo.dbo.tenant.SysTenantListByUserDO;
import com.smart.module.system.pojo.dto.tenant.SysListTenantFunctionDTO;
import com.smart.module.system.pojo.dto.tenant.SysListTenantRoleFunctionDTO;
import com.smart.module.system.pojo.dto.user.SysUserSetUseYnParameter;
import com.smart.module.system.pojo.dto.user.UserListDTO;
import com.smart.module.system.pojo.dto.user.UserSetRoleDTO;
import com.smart.module.system.pojo.vo.SysFunctionListVO;
import com.smart.module.system.pojo.vo.user.SysUserListVO;
import com.smart.module.system.pojo.vo.user.SysUserWithDataScopeDTO;
import com.smart.module.system.service.*;
import com.smart.module.system.service.tenant.SysTenantUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jackson
 * 2020/1/23 7:44 下午
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUserPO> implements SysUserService {

    /**
     * 密码加密盐值
     */
    private static final String SALT = "888888$#@";

    private static final String I18N_LEFT = "{";

    private static final String I18N_RIGHT = "}";

    private final SysUserRoleService sysUserRoleService;
    private final SysUserDeptService sysUserDeptService;
    private final SysUserGroupUserMapper sysUserGroupUserMapper;
    private final SysRoleService sysRoleService;
    private final SysRoleFunctionService sysRoleFunctionService;
    private final SysFunctionService sysFunctionService;
    private final UserSetterService userSetterService;
    private final SysUserAccountService sysUserAccountService;
    private final SysDeptService sysDeptService;
    private final SysTenantUserService sysTenantUserService;
    private final SysTenantMapper sysTenantMapper;
    private final SysParameterApi sysParameterApi;

    @Override
    public List<? extends SysUserPO> list(@NonNull QueryWrapper<SysUserPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        UserListDTO userListParameter = (UserListDTO) parameter;
        List<Long> deptIdList = userListParameter.getDeptIdList();
        if (!CollectionUtils.isEmpty(deptIdList)) {
            // 查询部门信息
            Set<Long> allDeptIds = this.sysDeptService.queryAllChildIds(new HashSet<>(deptIdList));
            allDeptIds.addAll(deptIdList);
            // 添加部门查询条件
            String deptStr = allDeptIds.stream().map(Object::toString).collect(Collectors.joining(","));
            queryWrapper.apply(String.format("user_id in (select m.user_id from sys_user_dept m where m.dept_id in (%s) and ident = 'USER_DEPT')", deptStr));
        }
        // 添加租户查询条件
        if (!AuthUtils.isPlatformTenant()) {
            if (userListParameter.getUseYn() == null) {
                queryWrapper.apply("user_id in (select M.user_id from sys_tenant_user M where M.tenant_id = {0})", AuthUtils.getNonNullCurrentTenantId());
            } else {
                queryWrapper.apply("user_id in (select M.user_id from sys_tenant_user M where M.tenant_id = {0} and M.use_yn = {1})", AuthUtils.getNonNullCurrentTenantId(), userListParameter.getUseYn());
            }
        }
        List<? extends SysUserPO> userList = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>(0);
        }
        List<SysUserListVO> voList = userList.stream()
                .map(item -> {
                    SysUserListVO vo = new SysUserListVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).toList();
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.QUERY_CREATE_UPDATE_USER.name()))) {
            this.userSetterService.setCreateUpdateUser(voList);
        }
        // 查询账户信息
        this.queryUserAccount(voList);
        return voList;
    }

    /**
     * 通过ID获取用户详情
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    @Override
    public SysUserListVO getDetailById(Long userId) {
        SysUserPO user = super.getById(userId);
        if (user == null) {
            return null;
        }
        SysUserListVO vo = new SysUserListVO();
        BeanUtils.copyProperties(user, vo);

        List<SysUserListVO> voList = List.of(vo);
        // 查询创建人和审批人
        this.userSetterService.setCreateUpdateUser(voList);
        // 查询账户信息
        this.queryUserAccount(voList);
        return voList.get(0);
    }

    /**
     * 查询用户账户信息
     * @param userList 用户列表
     */
    private void queryUserAccount(List<SysUserListVO> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        List<Long> userIdList = userList.stream().map(SysUserListVO::getUserId).toList();
        Map<Long, SysUserAccountPO> sysUserAccountMap = Lists.partition(userIdList, 400).stream()
                .flatMap(idList -> this.sysUserAccountService.list(
                        new QueryWrapper<SysUserAccountPO>().lambda()
                        .in(SysUserAccountPO::getUserId, idList)
                                .eq(SysUserAccountPO::getTenantId, AuthUtils.getNonNullCurrentTenantId())
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
                        .select(SysUserRolePO::getRoleId)
                        .eq(SysUserRolePO :: getUserId, userId)
                        .eq(SysUserRolePO :: getEnable, Boolean.TRUE)
        ).stream().map(SysUserRolePO :: getRoleId).collect(Collectors.toSet());
        roleIdSet.addAll(userRoleIdSet);
        // 2、查询用户组的角色
        // 查询用户组 TODO:该功能暂不启用
//        final Set<Long> userGroupIdSet = this.sysUserGroupUserMapper.selectList(
//                new QueryWrapper<SysUserGroupUserPO>().lambda()
//                        .select(SysUserGroupUserPO::getUserGroupId)
//                        .eq(SysUserGroupUserPO :: getUserId, userId)
//                        .eq(SysUserGroupUserPO :: getUseYn, Boolean.TRUE)
//        ).stream().map(SysUserGroupUserPO :: getUserGroupId).collect(Collectors.toSet());
//        // 通过用户组查询角色ID
//        if (!userGroupIdSet.isEmpty()) {
//            final Set<Long> groupRoleIdSet = this.sysUserGroupRoleMapper.selectList(
//                    new QueryWrapper<SysUserGroupRolePO>().lambda()
//                            .select(SysUserGroupRolePO :: getRoleId)
//                            .in(SysUserGroupRolePO :: getGroupId, userGroupIdSet)
//                            .eq(SysUserGroupRolePO :: getUseYn, Boolean.TRUE)
//            ).stream().map(SysUserGroupRolePO :: getRoleId).collect(Collectors.toSet());
//            roleIdSet.addAll(groupRoleIdSet);
//        }
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
    public boolean removeByIds(Collection<?> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        // 系统内置用户不能删除
        Long buildInCount = this.lambdaQuery()
                .in(SysUserPO::getUserId, idList)
                .eq(SysUserPO::getBuildIn, Boolean.TRUE)
                .count();
        if (buildInCount > 0) {
            throw new BusinessException("系统内置用户不能删除");
        }
        Lists.partition(Arrays.asList(idList.toArray()), 500).forEach(list -> {
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
            // 删除用户与租户关联关系
            this.sysTenantUserService.remove(
                    new QueryWrapper<SysTenantUserPO>().lambda()
                            .in(SysTenantUserPO::getUserId, list)
            );
            // 删除账户信息
            this.sysUserAccountService.remove(
                    Wrappers.lambdaQuery(SysUserAccountPO.class)
                            .in(SysUserAccountPO::getUserId, list)
            );
            super.removeByIds(list);
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
        // 获取默认密码
        String defaultPassword = this.sysParameterApi.getParameter(SysParameterCodeEnum.AUTH_DEFAULT_PASSWORD.getCode());
        entity.setPassword(this.createPassword(entity.getUsername(), defaultPassword));
        return super.save(entity);
    }

    /**
     * 创建密码密文
     * @param username 用户名
     * @param password 密码
     * @return 密码
     */
    protected String createPassword(String username, String password) {
        return DigestUtils.sha256(username + password + SALT, 2);
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
    private <T> void setWithUser(@NonNull List<T> resource, boolean withCreateUser, boolean withUpdateUser) {
        if (resource.isEmpty()) {
            return;
        }
        Set<Long> userIdSet = Sets.newHashSet();
        for (T item : resource) {
            if (withCreateUser) {
                // 获取创建人员ID
                userIdSet.add((Long) PropertyUtils.getProperty(item, ModelPropertyEnum.CREATE_USER_ID.getName()));
            }
            if (withUpdateUser) {
                // 获取创建人员ID
                userIdSet.add((Long) PropertyUtils.getProperty(item, ModelPropertyEnum.UPDATE_USER_ID.getName()));
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
                Long createUserId = (Long) PropertyUtils.getProperty(item, ModelPropertyEnum.CREATE_USER_ID.getName());
                PropertyUtils.setProperty(item, ModelPropertyEnum.CREATE_USER.getName(), userMap.get(createUserId));
            }
            if (withUpdateUser) {
                Long updateUserId = (Long) PropertyUtils.getProperty(item, ModelPropertyEnum.UPDATE_USER_ID.getName());
                PropertyUtils.setProperty(item, ModelPropertyEnum.UPDATE_USER.getName(), userMap.get(updateUserId));
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
        return this.listUserFunctionWithLocale(List.of(FunctionTypeEnum.CATALOG, FunctionTypeEnum.MENU), localeList);
    }

    /**
     * 查询用户角色权限信息
     * @param parameter 参数
     * @return 用户账户信息
     */
    @Override
    @Transactional(readOnly = true)
    public UserAccountData queryUserAccount(QueryUserAccountDTO parameter) {
        UserAccountData userAccountData = new UserAccountData();
        // 查询租户信息
        UserTenantDTO userTenant = this.queryUserTenant(parameter);
        if (userTenant == null) {
            return null;
        }
        if (Boolean.FALSE.equals(userTenant.getUseYn())) {
            return null;
        }
        userAccountData.setTenant(userTenant);
        SmartTenantHolder.set(userTenant);
        // 查询账户信息
        SysUserAccountPO sysUserAccount = this.sysUserAccountService.getOne(
                new LambdaQueryWrapper<>(SysUserAccountPO.class)
                        .eq(SysUserAccountPO::getUserId, parameter.getUserId())
                        .eq(SysUserAccountPO::getTenantId, userTenant.getTenantId())
        );
        if (sysUserAccount == null) {
            return userAccountData;
        }
        UserAccountDTO account = new UserAccountDTO();
        BeanUtils.copyProperties(sysUserAccount, account);
        userAccountData.setAccount(account);

        // 1、查询角色信息
        List<SysRolePO> sysRoleList = this.listRole(parameter.getUserId());
        if (CollectionUtils.isEmpty(sysRoleList)) {
            return userAccountData;
        }
        userAccountData.setRoleCodes(
                sysRoleList.stream()
                        .map(item -> new AuthRole(item.getRoleCode(), item.getRoleName(), item.getSuperAdminYn()))
                        .collect(Collectors.toSet())
        );
        List<SysFunctionPO> functionList = this.listPermissionFunctionIds(
                userTenant.getTenantId(),
                () -> sysRoleList.stream().map(SysRolePO::getRoleId).toList(),
                Boolean.TRUE.equals(userTenant.getPlatformYn()),
                sysRoleList.stream()
                        .anyMatch(item -> Boolean.TRUE.equals(item.getSuperAdminYn())),
                List.of(FunctionTypeEnum.FUNCTION)
        );

        if (CollectionUtils.isEmpty(functionList)) {
            return userAccountData;
        }
        Set<Permission> permissions = functionList.stream()
                .flatMap(item -> {
                    var url = item.getUrl();
                    if (StringUtils.isNotBlank(url)) {
                        return Arrays.stream(url.split(";"))
                                .map(uriItem -> Permission.builder()
                                        .method(item.getHttpMethod())
                                        .url(uriItem)
                                        .authority(item.getPermission())
                                        .build());
                    }
                    return Stream.of(Permission.builder()
                            .method(item.getHttpMethod())
                            .url(item.getUrl())
                            .authority(item.getPermission())
                            .build());
                }).collect(Collectors.toSet());
        userAccountData.setPermissions(permissions);
        return userAccountData;
    }

    /**
     * 查询用户的菜单
     * @param tenantId 用户租户
     * @param roleIdProvider 用户角色
     * @param isPlatformTenant 是否平台租户
     * @param isSuperAdmin 是否超级管理员
     * @param functionTypeList 功能类型
     * @return 功能列表
     */
    private List<SysFunctionPO> listPermissionFunctionIds(Long tenantId, Supplier<List<Long>> roleIdProvider, boolean isPlatformTenant, boolean isSuperAdmin, List<FunctionTypeEnum> functionTypeList) {
        if (CollectionUtils.isEmpty(functionTypeList)) {
            return List.of();
        }
        if (isPlatformTenant && isSuperAdmin) {
            // 平台租户管理员，直接返回所有菜单
            return this.sysFunctionService.lambdaQuery()
                    .in(SysFunctionPO :: getFunctionType, functionTypeList.stream().map(FunctionTypeEnum::getValue).toList())
                    .orderByAsc(SysFunctionPO :: getSeq)
                    .list();
        }
        Set<Long> functionIds = null;
        if (isSuperAdmin) {
            SysListTenantFunctionDTO parameter = new SysListTenantFunctionDTO();
            parameter.setTenantId(tenantId);
            functionIds = new HashSet<>(this.sysTenantUserService.listTenantFunctionIds(parameter));
        } else if (isPlatformTenant) {
            // 平台租户但不是管理员
            // 2、查询角色对应的功能ID
            List<Long> roleIdList = roleIdProvider.get();
            if (!CollectionUtils.isEmpty(roleIdList)) {
                functionIds = this.sysRoleFunctionService.list(
                        new QueryWrapper<SysRoleFunctionPO>().lambda()
                                .select(SysRoleFunctionPO::getFunctionId)
                                .in(SysRoleFunctionPO::getRoleId, roleIdProvider.get())
                ).stream().map(SysRoleFunctionPO::getFunctionId).collect(Collectors.toSet());
            }
        } else {
            // 非平台租户的普通角色
            SysListTenantRoleFunctionDTO parameter = new SysListTenantRoleFunctionDTO();
            parameter.setTenantId(tenantId);
            parameter.setRoleIdList(roleIdProvider.get());
            functionIds = new HashSet<>(this.sysTenantUserService.listTenantRoleFunctionIds(parameter));
        }

        if (CollectionUtils.isEmpty(functionIds)) {
            return List.of();
        }
        return CrudUtils.partitionList(new ArrayList<>(functionIds), 900, ids -> this.sysFunctionService.lambdaQuery()
                .in(SysFunctionPO::getFunctionId, ids)
                .in(SysFunctionPO::getFunctionType, functionTypeList.stream().map(FunctionTypeEnum::getValue).toList())
                .orderByAsc(SysFunctionPO :: getSeq)
                .list());
    }

    /**
     * 查询租户信息
     * @param parameter 参数
     * @return 租户信息
     */
    private UserTenantDTO queryUserTenant(QueryUserAccountDTO parameter) {
        if (parameter.getTenantId() == null) {
            // 查询用户默认租户或第一个租户
            SysTenantListByUserDO tenant = this.sysTenantUserService.selectOneTenantByUser(parameter.getUserId());
            if (tenant == null) {
                return null;
            }
            UserTenantDTO tenantDto = new UserTenantDTO();
            BeanUtils.copyProperties(tenant, tenantDto);
            return tenantDto;
        }
        SysTenantPO sysTenant = this.sysTenantMapper.selectById(parameter.getTenantId());
        if (sysTenant == null) {
            return null;
        }
        SysTenantUserPO sysTenantUser = this.sysTenantUserService.lambdaQuery()
                .eq(SysTenantUserPO::getUserId, parameter.getUserId())
                .eq(SysTenantUserPO::getTenantId, parameter.getTenantId())
                .one();
        if (sysTenantUser == null) {
            return null;
        }
        UserTenantDTO tenantDto = new UserTenantDTO();
        BeanUtils.copyProperties(sysTenant, tenantDto);
        tenantDto.setTenantId(parameter.getTenantId());
        tenantDto.setUseYn(sysTenantUser.getUseYn());
        return tenantDto;
    }

    /**
     * 查询用户功能
     * @param types 查询的功能类型
     * @return 用户ID表
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<SysFunctionPO> listUserFunction(@NonNull List<FunctionTypeEnum> types) {
        Long userId = AuthUtils.getNonNullCurrentUserId();
        return this.listPermissionFunctionIds(
                AuthUtils.getNonNullCurrentTenantId(),
                () -> this.listRole(userId)
                        .stream().map(SysRolePO::getRoleId)
                        .toList(),
                AuthUtils.isPlatformTenant(),
                AuthUtils.isSuperAdmin(),
                types
        );
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<SysFunctionListVO> listUserFunctionWithLocale(List<FunctionTypeEnum> types, List<Locale> localeList) {
        List<SysFunctionPO> functionList = this.listUserFunction(types);
        if (CollectionUtils.isEmpty(functionList)) {
            return new ArrayList<>(0);
        }
        return functionList.stream().map(item -> {
            SysFunctionListVO vo = new SysFunctionListVO();
            BeanUtils.copyProperties(item, vo);
            // 获取国际化信息
            if (!CollectionUtils.isEmpty(localeList) && StringUtils.isNotBlank(item.getI18nCode()) && item.getI18nCode().startsWith(I18N_LEFT) && item.getI18nCode().endsWith(I18N_RIGHT)) {
                String i18nCode = item.getI18nCode().replace(I18N_LEFT, "").replace(I18N_RIGHT, "");
                vo.setLocales(
                        localeList.stream().collect(Collectors.toMap(Locale::toString, locale -> {
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
        }).toList();
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
        if (!CollectionUtils.isEmpty(parameter.getRoleIdList())) {
            this.sysUserRoleService.saveBatch(
                    parameter.getRoleIdList().stream().map(roleId -> SysUserRolePO.builder()
                            .roleId(roleId)
                            .enable(Boolean.TRUE)
                            .userId(parameter.getUserId())
                            .build()).toList()
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

    @Override
    public List<SysUserWthAccountBO> listUserWithAccount(QueryWrapper<SysUserPO> parameter) {
        return this.baseMapper.listUserWithAccount(parameter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUpdateWithDataScope(SysUserWithDataScopeDTO parameter) {
        // 更新用户
        var userModel = new SysUserPO();
        BeanUtils.copyProperties(parameter, userModel);
        boolean isAdd = this.isAdd(userModel);
        Long userId = userModel.getUserId() == null ? SmartIdGenerator.nextId() : userModel.getUserId();
        if (parameter.getDeptId() != null) {
            SysUserDeptPO dataScopeModel = new SysUserDeptPO();
            dataScopeModel.setUserId(userId);
            dataScopeModel.setDeptId(parameter.getDeptId());
            // 设置标识位
            dataScopeModel.setIdent(UserDeptIdentEnum.USER_DEPT);
            // 将数据权限数组转为逗号分隔字符串
            if (!CollectionUtils.isEmpty(parameter.getDataScopeList())) {
                dataScopeModel.setDataScope(
                        parameter.getDataScopeList().stream()
                                .map(Enum::toString)
                                .collect(Collectors.joining(","))
                );
            }
            if (userModel.getUserId() != null) {
                // 删除之前的部门数据权限
                this.sysUserDeptService.remove(
                        new QueryWrapper<SysUserDeptPO>().lambda()
                                .eq(SysUserDeptPO::getUserId, userModel.getUserId())
                                .eq(SysUserDeptPO::getIdent, UserDeptIdentEnum.USER_DEPT)
                );
            }
            this.sysUserDeptService.save(dataScopeModel);
        }
        // 保存用户与租户关联关系
        if (isAdd) {
            userModel.setUserId(userId);
            SysTenantUserPO tenantUser = new SysTenantUserPO();
            tenantUser.setUserId(userId);
            tenantUser.setTenantId(AuthUtils.getNonNullCurrentTenantId());
            tenantUser.setDefaultYn(Boolean.FALSE);
            this.sysTenantUserService.save(tenantUser);

            return this.save(userModel);
        }
        // 执行更新操作
        return this.updateById(userModel);
    }

    @Override
    public SysUserWithDataScopeDTO getByIdWithDataScope(Long userId) {
        var sysUser = this.getById(userId);
        if (sysUser == null) {
            return null;
        }
        var vo = new SysUserWithDataScopeDTO();
        BeanUtils.copyProperties(sysUser, vo);
        // 查询用户数据权限
        var deptList = this.sysUserDeptService.list(
                new QueryWrapper<SysUserDeptPO>().lambda()
                        .select(SysUserDeptPO::getDeptId, SysUserDeptPO::getDataScope, SysUserDeptPO::getUserId)
                        .eq(SysUserDeptPO::getUserId, userId)
                        .eq(SysUserDeptPO::getIdent, UserDeptIdentEnum.USER_DEPT)
        );
        if (CollectionUtils.isEmpty(deptList)) {
            return vo;
        }
        var dept = deptList.get(0);
        vo.setDeptId(dept.getDeptId());
        vo.setDataScopeList(
                Arrays.stream(dept.getDataScope().split(","))
                        .filter(StringUtils::isNotBlank)
                        .map(DataPermissionScope::valueOf)
                        .toList()
        );
        return vo;
    }

    /**
     * 通过手机号查询用户
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    @Override
    public SysUserPO getByMobile(@NonNull String mobile) {
        return this.getOne(
                new QueryWrapper<SysUserPO>().lambda()
                        .eq(SysUserPO::getMobile, mobile)
                        .eq(SysUserPO::getUseYn, Boolean.TRUE)
        );
    }

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public SysUserPO getByUsername(@NonNull String username) {
        return this.getOne(
                new QueryWrapper<SysUserPO>().lambda()
                        .eq(SysUserPO::getUsername, username)
                        .eq(SysUserPO::getUseYn, Boolean.TRUE)
        );
    }

    /**
     * 重置用户密码
     *
     * @param userId 用户ID
     * @return 重置后的密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String resetPassword(Long userId) {
        SysUserPO sysUser = this.getById(userId);
        if (sysUser == null) {
            throw new SystemException("通过ID查询用户失败，ID：" + userId);
        }
        String password = RandomStringUtils.randomAlphabetic(15);
        String secretPassword = this.createPassword(sysUser.getUsername(), password);
        this.update(
                new UpdateWrapper<SysUserPO>().lambda()
                        .set(SysUserPO::getPassword, secretPassword)
                        .eq(SysUserPO::getUserId, userId)
        );
        return password;
    }

    /**
     * 批量查询用户的角色信息
     *
     * @param userIdList 用户ID列表
     * @return 用户角色细腻系
     */
    @Override
    public Map<Long, List<SysRolePO>> listUserRole(List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return Collections.emptyMap();
        }
        List<SysUserRolePO> sysUserRoleList = this.sysUserRoleService.list(
                new QueryWrapper<SysUserRolePO>().lambda()
                        .in(SysUserRolePO::getUserId, userIdList)
                        .eq(SysUserRolePO::getEnable, Boolean.TRUE)
        );
        if (CollectionUtils.isEmpty(sysUserRoleList)) {
            return Collections.emptyMap();
        }
        Set<Long> roleIds = sysUserRoleList.stream().map(SysUserRolePO::getRoleId).collect(Collectors.toSet());
        Map<Long, SysRolePO> roleIdMap = this.sysRoleService.list(
                new QueryWrapper<SysRolePO>().lambda()
                        .in(SysRolePO::getRoleId, roleIds)
                        .eq(SysRolePO::getUseYn, Boolean.TRUE)
        ).stream().collect(Collectors.toMap(SysRolePO::getRoleId, item -> item));
        if (CollectionUtils.isEmpty(roleIdMap)) {
            return Collections.emptyMap();
        }
        return sysUserRoleList.stream()
                .collect(
                        Collectors.groupingBy(
                                SysUserRolePO::getUserId,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.stream()
                                                .map(item -> roleIdMap.get(item.getRoleId()))
                                                .toList()
                                )
                        )
                );
    }

    /**
     * 设置启停状态
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setUseYn(@NonNull SetUseYnParameter parameter) {
        SysUserSetUseYnParameter userParameter = (SysUserSetUseYnParameter) parameter;
        List<Long> tenantIdList = userParameter.getTenantIdList();
        if (CollectionUtils.isEmpty(tenantIdList)) {
            tenantIdList = List.of(AuthUtils.getNonNullCurrentTenantId());
        }
        RestUserDetails currentUser = AuthUtils.getNonNullCurrentUser();
        return this.sysTenantUserService.update(
                new LambdaUpdateWrapper<>(SysTenantUserPO.class)
                        .set(SysTenantUserPO::getUseYn, parameter.getUseYn())
                        .set(SysTenantUserPO::getUpdateUserId, currentUser.getUserId())
                        .set(SysTenantUserPO::getUpdateBy, currentUser.getFullName())
                        .set(SysTenantUserPO::getUpdateTime, LocalDateTime.now())
                        .in(SysTenantUserPO::getUserId, parameter.getIdList())
                        .in(SysTenantUserPO::getTenantId, tenantIdList)
        );
    }
}
