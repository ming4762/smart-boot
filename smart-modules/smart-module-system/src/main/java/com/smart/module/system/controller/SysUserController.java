package com.smart.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.smart.framework.auth.common.annotation.NonUrlCheck;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.data.Tree;
import com.smart.framework.commons.core.http.HttpStatus;
import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.commons.core.utils.TreeUtils;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.plus.tenant.SmartTenantControl;
import com.smart.framework.crud.query.IdParameter;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.parameter.UserAccountUnLockParameter;
import com.smart.module.system.constants.SystemConstantEnum;
import com.smart.module.system.constants.UserDeptIdentEnum;
import com.smart.module.system.model.*;
import com.smart.module.system.pojo.dto.user.*;
import com.smart.module.system.pojo.vo.SysFunctionListVO;
import com.smart.module.system.pojo.vo.user.SysUserListVO;
import com.smart.module.system.pojo.vo.user.SysUserWithDeptDTO;
import com.smart.module.system.service.SysUserAccountService;
import com.smart.module.system.service.SysUserDeptService;
import com.smart.module.system.service.SysUserRoleService;
import com.smart.module.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户controller层
 * @author shizhongming
 * 2020/1/24 2:22 下午
 */
@RestController
@RequestMapping("sys/user")
@Tag(name = "用户管理")
@NonUrlCheck
@RequiredArgsConstructor
public class SysUserController extends BaseController<SysUserService, SysUserPO> {

    private final SysUserRoleService sysUserRoleService;
    private final SysUserAccountService sysUserAccountService;
    private final SysUserDeptService sysUserDeptService;
    private final SystemAuthUserApi systemAuthUserApi;


    /**
     * 添加保存方法
     * @param parameter 用户实体
     * @return 是否保存成功
     */
    @PostMapping("saveUpdate")
    @Operation(summary = "添加/更新用户")
    @Log(value = "添加/更新用户", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:user', 'save') or hasPermission('sys:user', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody @Valid UserUpdateDTO parameter) {
        SysUserPO model = new SysUserPO();
        BeanUtils.copyProperties(parameter, model);
        return Result.success(this.service.saveOrUpdate(model));
    }

    @PostMapping("saveUpdateWithDept")
    @Operation(summary = "添加/更新用户(带部门信息)")
    @Log(value = "添加/更新用户(带部门信息)", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:user', 'save') or hasPermission('sys:user', 'update')")
    public Result<Boolean> saveUpdateWithDept(@RequestBody @Valid UserSaveUpdateWithDeptDTO parameter) {
        return Result.success(this.service.saveUpdateWithDept(parameter));
    }

    @PostMapping("getById")
    @Operation(summary = "通过ID查询")
    @Override
    public Result<SysUserPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 通过ID批量获取
     *
     * @param ids ID列表
     * @return list
     */
    @Override
    @PostMapping("listById")
    @Operation(summary = "通过ID批量查询")
    public Result<List<SysUserPO>> listById(@RequestBody List<? extends Serializable> ids) {
        return super.listById(ids);
    }

    @Override
    @PostMapping("save")
    @Operation(summary = "添加用户")
    @Log(value = "添加用户", type = LogOperationTypeEnum.ADD)
    @PreAuthorize("hasPermission('sys:user', 'save')")
    public Result<Boolean> save(@RequestBody @Valid SysUserPO model) {
        return Result.success(this.service.save(model));
    }

    @Override
    @PostMapping("update")
    @Operation(summary = "更新用户")
    @Log(value = "更新用户", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:user', 'update')")
    public Result<Boolean> update(@RequestBody SysUserPO model) {
        return super.update(model);
    }


    @PostMapping("list")
    @Operation(summary = "查询用户列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull UserListDTO parameter) {
        if (parameter.getTenantId() == null) {
            parameter.setTenantId(AuthUtils.getCurrentTenantId());
        }
        return super.list(parameter);
    }

    @PostMapping("listWithAccount")
    @Operation(summary = "查询用户列表（支持分页、实体类属性查询）")
    public Result<Object> listWithAccount(@RequestBody @NonNull UserListDTO parameter) {
        if (parameter.getTenantId() == null) {
            parameter.setTenantId(AuthUtils.getCurrentTenantId());
        }
        parameter.getParameter().put(SystemConstantEnum.LIST_USER_WITH_ACCOUNT.name(), Boolean.TRUE);
        return super.list(parameter);
    }

    @PostMapping("listByTenant")
    @Operation(summary = "查询用户列表（支持分页、实体类属性查询）,根据自定义租户过滤")
    public Result<Object> listByTenant(@RequestBody @NonNull UserListDTO parameter) {
        parameter.getParameter().put(SystemConstantEnum.LIST_FILTER_TENANT.name(), Boolean.TRUE);
        return super.list(parameter);
    }

    /**
     * 通过ID批量删除
     * @param idList ID集合
     * @return 是否删除成功
     */
    @PreAuthorize("hasPermission('sys:user', 'delete')")
    @Operation(summary = "通过ID批量删除用户")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除用户", type = LogOperationTypeEnum.DELETE)
    @Override
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (idList.isEmpty()) {
            return Result.ofStatus(HttpStatus.PARAM_NOT_NULL, "用户ID集合不能为空");
        }
        return Result.success(this.service.removeByIds(idList));
    }

    /**
     * 查询用户菜单信息
     * @return 用户菜单
     */
    @Operation(summary = "查询用户菜单信息")
    @PostMapping("listUserMenu")
    public Result<List<SysFunctionListVO>> listUserMenu(@RequestBody List<Locale> localeList) {
        return Result.success(this.service.listCurrentUserMenu(localeList));
    }

    /**
     * 查询用户菜单树
     * @return 用户菜单树
     */
    @Operation(summary = "查询用户菜单树")
    @PostMapping("listUserMenuTree")
    public Result<List<Tree<SysFunctionPO>>> listUserMenuTree(@RequestBody List<Locale> localeList) {
        final List<SysFunctionListVO> sysFunctionList = this.service.listCurrentUserMenu(localeList);
        if (CollectionUtils.isEmpty(sysFunctionList)) {
            return Result.success(Lists.newArrayList());
        }
        return Result.success(TreeUtils.buildList(
                sysFunctionList.stream().map(item -> {
                    final Tree<SysFunctionPO> tree = new Tree<>();
                    tree.setId(item.getFunctionId());
                    tree.setData(item);
                    tree.setText(item.getFunctionName());
                    tree.setParentId(item.getParentId());
                    return tree;
                }).toList(),
                0L
        ));
    }

    /**
     * 通过用户ID查询角色ID
     * @param userId 用户ID
     * @return 角色ID
     */
    @Operation(summary = "查询角色ID列表")
    @PostMapping("listRoleId")
    public Result<Set<Long>> listRoleId(@RequestBody Long userId) {
        if (Objects.isNull(userId)) {
            return Result.success(Sets.newHashSet());
        }
        return Result.success(
                this.sysUserRoleService.list(
                        new QueryWrapper<SysUserRolePO>().lambda()
                        .select(SysUserRolePO :: getRoleId)
                        .eq(SysUserRolePO :: getUserId, userId)
                ).stream().map(SysUserRolePO :: getRoleId)
                .collect(Collectors.toSet())
        );
    }

    @PostMapping("setRole")
    @Log(value = "设置角色", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:user', 'setRole')")
    @Operation(summary = "设置角色")
    public Result<Boolean> setRole(@RequestBody @Valid UserSetRoleDTO parameter) {
        return Result.success(this.service.setRole(parameter));
    }

    /**
     * 通过角色ID查询用户信息
     * @param roleIdList 角色ID列表
     * @return 用户信息
     */
    @PostMapping("listUserByRoleId")
    @Operation(summary = "通过角色ID查询用户信息")
    public Result<List<SysUserPO>> listUserByRoleId(@RequestBody List<Long> roleIdList) {
        return Result.success(this.service.listUserByRoleId(roleIdList));
    }

    @PostMapping("listUserByRoleTenant")
    @Operation(summary = "通过角色ID&租户ID查询用户信息")
    public Result<List<SysUserPO>> listUserByRoleTenant(@RequestBody @Valid ListUserByRoleTenantDTO parameter)   {
        // 忽略租户控制
        SmartTenantControl.ignore(SysUserRolePO.class, null, List.of(SqlCommandType.SELECT));
        if (parameter.getTenantId() == null) {
            parameter.setTenantId(AuthUtils.getNonNullCurrentTenantId());
        }
        return Result.success(this.service.listUserByRoleTenant(parameter));
    }

    /**
     * 设置启用停用
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @PostMapping("setUseYn")
    @Log(value = "设置用户启停状态", type = LogOperationTypeEnum.UPDATE)
    @Operation(summary = "设置用户启停状态")
    @PreAuthorize("hasPermission('sys:user', 'setUseYn')")
    public Result<Boolean> setUseYn(@RequestBody @Valid SysUserSetUseYnParameter parameter) {
        if (!AuthUtils.isPlatformTenant() && !CollectionUtils.isEmpty(parameter.getTenantIdList())) {
            throw new AccessDeniedException("非平台租户无权停用其他租户用户");
        }
        return super.setUseYn(parameter);
    }

    /**
     * 批量创建账户接口
     * @param userIdList 用户ID列表
     * @return 是否创建成功
     */
    @PostMapping("createAccount")
    @Operation(summary = "批量创建账户")
    @Log(value = "创建账户", type = LogOperationTypeEnum.ADD)
    @PreAuthorize("hasPermission('sys:account', 'add')")
    public Result<Boolean> createAccount(@RequestBody List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return Result.success(false);
        }
        return Result.success(this.sysUserAccountService.createAccount(AuthUtils.getNonNullCurrentTenantId(), userIdList));
    }

    @PostMapping("saveAccountSetting")
    @Operation(summary = "保存账户配置信息")
    @Log(value = "保存账户配置信息", type = LogOperationTypeEnum.ADD)
    @PreAuthorize("hasPermission('sys:account', 'update')")
    public Result<Boolean> saveAccountSetting(@RequestBody @Valid UserAccountSaveDTO parameter) {
        SysUserAccountPO account = new SysUserAccountPO();
        BeanUtils.copyProperties(parameter, account);
        this.sysUserAccountService.update(
                account,
                new LambdaQueryWrapper<>(SysUserAccountPO.class).eq(SysUserAccountPO::getUserId, account.getUserId())
                        .eq(SysUserAccountPO::getTenantId, AuthUtils.getNonNullCurrentTenantId())
        );
        return Result.success(this.sysUserAccountService.updateById(account));
    }

    @PostMapping("queryUserDeptPermission")
    @Operation(summary = "查询用户部门数据权限信息")
    @PreAuthorize("hasPermission('sys:user', 'query')")
    public Result<SysUserDeptPO> queryUserDeptPermission(@RequestBody Long userId) {
        var dataList = this.sysUserDeptService.list(
                new QueryWrapper<SysUserDeptPO>().lambda()
                        .select(SysUserDeptPO::getDeptId, SysUserDeptPO::getDataScope, SysUserDeptPO::getUserId)
                        .eq(SysUserDeptPO::getUserId, userId)
                        .eq(SysUserDeptPO::getIdent, UserDeptIdentEnum.USER_DEPT)
        );
        if (CollectionUtils.isEmpty(dataList)) {
            return Result.success();
        }
        return Result.success(dataList.getFirst());
    }

    @PostMapping("unlockUserAccount")
    @Operation(summary = "解锁账号")
    @PreAuthorize("hasPermission('sys:user', 'unlockUserAccount')")
    @Log(value = "解锁账号", type = LogOperationTypeEnum.UPDATE)
    public Result<Boolean> unlockUserAccount(@RequestBody IdParameter parameter) {
        return Result.success(this.systemAuthUserApi.unlockAccount(new UserAccountUnLockParameter(parameter.getId(), null)));
    }

    @PostMapping("resetPassword")
    @Operation(summary = "重置用户密码")
    @PreAuthorize("hasPermission('sys:user', 'resetPassword')")
    @Log(value = "解锁账号", type = LogOperationTypeEnum.UPDATE)
    public Result<String> resetPassword(@RequestBody IdParameter parameter) {
        return Result.success(this.service.resetPassword(parameter.getId()));
    }

    @PostMapping("listUserRole")
    @Operation(summary = "查询用户的角色信息")
    public Result<List<SysRolePO>> listUserRole(@RequestBody IdParameter parameter) {
        return Result.success(
                this.service.listUserRole(List.of(parameter.getId())).getOrDefault(parameter.getId(), Collections.emptyList())
        );
    }

    @PostMapping("getDetailById")
    @Operation(summary = "通过ID查询用户详情")
    public Result<SysUserListVO> getDetailById(@RequestBody Long id) {
        return Result.success(this.service.getDetailById(id));
    }

    @PostMapping("getUserByIdWithDept")
    @Operation(summary = "通过ID查询用户详情")
    public Result<SysUserWithDeptDTO> getUserByIdWithDept(@RequestBody Long id) {
        return Result.success(this.service.getUserByIdWithDept(id));
    }
}
