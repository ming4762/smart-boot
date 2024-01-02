package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.dto.auth.Permission;
import com.smart.commons.core.dto.auth.UserRolePermission;
import com.smart.commons.core.exception.SystemException;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.utils.DigestUtils;
import com.smart.commons.core.utils.PropertyUtils;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.constants.UserPropertyEnum;
import com.smart.crud.datapermission.DataPermissionScope;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
import com.smart.system.constants.FunctionTypeEnum;
import com.smart.system.constants.UserDeptIdentEnum;
import com.smart.system.mapper.SysUserGroupRoleMapper;
import com.smart.system.mapper.SysUserGroupUserMapper;
import com.smart.system.mapper.SysUserMapper;
import com.smart.system.model.*;
import com.smart.system.pojo.dbo.SysUserWthAccountBO;
import com.smart.system.pojo.dto.user.UserListDTO;
import com.smart.system.pojo.dto.user.UserSetRoleDTO;
import com.smart.system.pojo.vo.SysFunctionListVO;
import com.smart.system.pojo.vo.user.SysUserListVO;
import com.smart.system.pojo.vo.user.SysUserWithDataScopeDTO;
import com.smart.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jackson
 * 2020/1/23 7:44 下午
 */
@Service
@Slf4j
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUserPO> implements SysUserService {

    private static final String SYSTEM_USER_TYPE = "SYSTEM_USER";

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

    private final SysUserDeptService sysUserDeptService;

    private final SysUserGroupUserMapper sysUserGroupUserMapper;

    private final SysUserGroupRoleMapper sysUserGroupRoleMapper;

    private SysRoleService sysRoleService;

    private final SysRoleFunctionService sysRoleFunctionService;

    private SysFunctionService sysFunctionService;

    private final UserSetterService userSetterService;

    private final SysUserAccountService sysUserAccountService;

    private final SysDeptService sysDeptService;

    public SysUserServiceImpl(SysUserRoleService sysUserRoleService, SysUserGroupUserMapper sysUserGroupUserMapper, SysUserGroupRoleMapper sysUserGroupRoleMapper, SysRoleFunctionService sysRoleFunctionService, UserSetterService userSetterService, SysUserAccountService sysUserAccountService, SysUserDeptService sysUserDeptService, SysDeptService sysDeptService) {
        this.sysUserRoleService = sysUserRoleService;
        this.sysUserGroupUserMapper = sysUserGroupUserMapper;
        this.sysUserGroupRoleMapper = sysUserGroupRoleMapper;
        this.sysRoleFunctionService = sysRoleFunctionService;
        this.userSetterService = userSetterService;
        this.sysUserAccountService = sysUserAccountService;
        this.sysUserDeptService = sysUserDeptService;
        this.sysDeptService = sysDeptService;
    }

    @Override
    public List<? extends SysUserPO> list(@NonNull QueryWrapper<SysUserPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        if (parameter instanceof UserListDTO) {
            List<Long> deptIdList = ((UserListDTO) parameter).getDeptIdList();
            if (!CollectionUtils.isEmpty(deptIdList)) {
                // 查询部门信息
                Set<Long> allDeptIds = this.sysDeptService.queryAllChildIds(new HashSet<>(deptIdList));
                allDeptIds.addAll(deptIdList);
                // 添加部门查询条件
                String deptStr = allDeptIds.stream().map(Object::toString).collect(Collectors.joining(","));
                queryWrapper.apply(String.format("user_id in (select m.user_id from sys_user_dept m where m.dept_id in (%s) and ident = 'USER_DEPT')", deptStr), allDeptIds);
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

    @Override
    public SysUserPO getById(Serializable id) {
        SysUserPO user = super.getById(id);
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
        // 查询用户组
        final Set<Long> userGroupIdSet = this.sysUserGroupUserMapper.selectList(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                        .select(SysUserGroupUserPO::getUserGroupId)
                    .eq(SysUserGroupUserPO :: getUserId, userId)
                    .eq(SysUserGroupUserPO :: getUseYn, Boolean.TRUE)
        ).stream().map(SysUserGroupUserPO :: getUserGroupId).collect(Collectors.toSet());
        // 通过用户组查询角色ID
        if (!userGroupIdSet.isEmpty()) {
            final Set<Long> groupRoleIdSet = this.sysUserGroupRoleMapper.selectList(
                    new QueryWrapper<SysUserGroupRolePO>().lambda()
                            .select(SysUserGroupRolePO :: getRoleId)
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
    public boolean removeByIds(Collection<?> idList) {
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
        entity.setPassword(this.createPassword(entity.getUsername(), DEFAULT_PASSWORD));
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
        return this.listUserFunctionWithLocale(userDetails.getUserId(), List.of(FunctionTypeEnum.CATALOG, FunctionTypeEnum.MENU), localeList);
    }

    @Override
    @Transactional(readOnly = true)
    public UserRolePermission queryUserRolePermission(@NonNull Long userId, @NonNull List<FunctionTypeEnum> types) {
        UserRolePermission userRolePermission = new UserRolePermission();
        // 1、查询角色信息
        List<SysRolePO> sysRoleList = this.listRole(userId);
        if (CollectionUtils.isEmpty(sysRoleList)) {
            return userRolePermission;
        }
        userRolePermission.setRoleCodes(
                sysRoleList.stream().map(SysRolePO::getRoleCode).collect(Collectors.toSet())
        );
        var roleIds = sysRoleList.stream().map(SysRolePO::getRoleId).collect(Collectors.toSet());
        // 2、查询角色对应的功能ID
        var functionIds = this.sysRoleFunctionService.list(
                new QueryWrapper<SysRoleFunctionPO>().lambda()
                        .select(SysRoleFunctionPO :: getFunctionId)
                        .in(SysRoleFunctionPO :: getRoleId, roleIds)
        ).stream().map(SysRoleFunctionPO :: getFunctionId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(functionIds)) {
            return userRolePermission;
        }
        //3、查询function列表
        var queryWrapper = new QueryWrapper<SysFunctionPO>().lambda()
                .select(SysFunctionPO::getUrl, SysFunctionPO::getPermission, SysFunctionPO::getHttpMethod)
                .in(SysFunctionPO :: getFunctionId, functionIds)
                .orderByAsc(SysFunctionPO :: getSeq);
        if (!CollectionUtils.isEmpty(types)) {
            queryWrapper.in(SysFunctionPO :: getFunctionType, types.stream().map(FunctionTypeEnum::getValue).toList());
        }
        var permissions = this.sysFunctionService.list(queryWrapper).stream()
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
        userRolePermission.setPermissions(permissions);
        return userRolePermission;
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
        if (!CollectionUtils.isEmpty(types)) {
            queryWrapper.in(SysFunctionPO :: getFunctionType, types.stream().map(FunctionTypeEnum::getValue).toList());
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
            if (!CollectionUtils.isEmpty(localeList) && StringUtils.isNotBlank(item.getI18nCode()) && item.getI18nCode().startsWith(I18N_LEFT) && item.getI18nCode().endsWith(I18N_RIGHT)) {
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
                            .build()).collect(Collectors.toList())
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUpdateWithDataScope(SysUserWithDataScopeDTO parameter) {
        // 更新用户
        var userModel = new SysUserPO();
        BeanUtils.copyProperties(parameter, userModel);

        // 判断是否是系统用户，系统用户没有数据权限
        if (SYSTEM_USER_TYPE.equals(userModel.getUserType())) {
            return this.saveOrUpdate(userModel);
        }
        if (parameter.getDeptId() != null) {
            SysUserDeptPO dataScopeModel = new SysUserDeptPO();
            dataScopeModel.setUserId(userModel.getUserId());
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
            // 删除之前的部门数据权限
            this.sysUserDeptService.remove(
                    new QueryWrapper<SysUserDeptPO>().lambda()
                            .eq(SysUserDeptPO::getUserId, userModel.getUserId())
                            .eq(SysUserDeptPO::getIdent, UserDeptIdentEnum.USER_DEPT)
            );
            this.sysUserDeptService.saveOrUpdate(dataScopeModel);
        }

        // 执行更新操作
        return this.saveOrUpdate(userModel);
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
}
