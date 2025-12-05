package com.smart.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.smart.framework.auth.common.annotation.NonUrlCheck;
import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.PageData;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.datapermission.handler.SmartDataPermissionMapperHolder;
import com.smart.framework.crud.plus.tenant.SmartTenantControl;
import com.smart.framework.crud.plus.tenant.SmartTenantIgnoreData;
import com.smart.framework.crud.query.IdParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.utils.CrudPageHelper;
import com.smart.module.system.model.SysRoleFunctionPO;
import com.smart.module.system.model.SysRolePO;
import com.smart.module.system.pojo.dto.role.*;
import com.smart.module.system.service.SysRoleFunctionService;
import com.smart.module.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 角色业务层
 * @author shizhongming
 * 2020/1/24 2:21 下午
 */
@RestController
@RequestMapping("sys/role")
@NonUrlCheck
@Tag(name = "系统角色管理")
public class SysRoleController extends BaseController<SysRoleService, SysRolePO> {

    private final SysRoleFunctionService sysRoleFunctionService;

    public SysRoleController(SysRoleFunctionService sysRoleFunctionService) {
        this.sysRoleFunctionService = sysRoleFunctionService;
    }

    @Override
    @PostMapping("save")
    @Operation(summary = "添加角色")
    @Log(value = "添加角色", type = LogOperationTypeEnum.ADD)
    @PreAuthorize("hasPermission('sys:role', 'save')")
    public Result<Boolean> save(@RequestBody SysRolePO model) {
        return Result.success(this.service.save(model));
    }

    @Override
    @PostMapping("update")
    @Operation(summary = "更新角色")
    @Log(value = "更新角色", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:role', 'update')")
    public Result<Boolean> update(@RequestBody SysRolePO model) {
        return super.update(model);
    }


    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }


    /**
     * 批量保存/更新
     *
     * @param modelList com.smart.framework.tool.code.model
     */
    @Override
    @Operation(summary = "添加修改角色")
    @PostMapping("batchSaveUpdate")
    @Log(value = "添加修改角色", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:role', 'save') or hasPermission('sys:role', 'update')")
    public Result<Boolean> batchSaveUpdate(@RequestBody List<SysRolePO> modelList) {
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @PreAuthorize("hasPermission('sys:role', 'delete')")
    @Operation(summary = "通过ID批量删除角色")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除角色", type = LogOperationTypeEnum.DELETE)
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SysRolePO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 获取角色对应的功能ID集合
     * @param parameter 角色ID
     * @return 功能ID集合
     */
    @Operation(summary = "获取角色对应的功能ID集合")
    @PostMapping("listFunctionId")
    public Result<List<Long>> listFunctionId(@RequestBody IdParameter parameter) {
        return Result.success(
                this.sysRoleFunctionService.listRoleFunction(
                        new QueryWrapper<SysRoleFunctionPO>()
                                .eq("B.has_child", 0).lambda()
                                .eq(SysRoleFunctionPO :: getRoleId, parameter.getId())
                                .eq(SysRoleFunctionPO::getHalfYn, Boolean.FALSE)
                )
                .stream().map(SysRoleFunctionPO :: getFunctionId)
                .toList()
        );
    }


    @Operation(summary = "保存角色功能")
    @PostMapping("saveRoleMenu")
    @Log(value = "保存角色功能", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:role', 'setFunction')")
    public Result<Boolean> saveRoleMenu(@RequestBody @Valid RoleMenuSaveDTO parameter) {
        return Result.success(this.service.saveRoleMenu(parameter));
    }

    @Operation(summary = "设置角色对应的用户")
    @PostMapping("setRoleUser")
    @Log(value = "设置角色对应的用户", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:role', 'setRoleUser')")
    public Result<Boolean> setRoleUser(@RequestBody @Valid RoleSetUserDTO parameter) {
        return Result.success(this.service.setRoleUser(parameter));
    }

    @PostMapping("setRoleUserWithTenant")
    @Log(value = "设置角色对应的用户，手动指定租户", type = LogOperationTypeEnum.UPDATE)
    @Operation(summary = "设置角色对应的用户，手动指定租户")
    public Result<Boolean> setRoleUserWithTenant(@RequestBody @Valid RoleSetUserWithTenantDTO parameter) {
        return Result.success(this.service.setRoleUserWithTenant(parameter));
    }

    @Operation(summary = "设置角色的数据权限")
    @Log(value = "设置角色对应的用户", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:role', 'setRoleDataPermission')")
    @PostMapping("setRoleDataPermission")
    public Result<Boolean> setRoleDataPermission(@RequestBody @Valid RoleSetDataPermissionDTO parameter) {
        SmartDataPermissionMapperHolder.clear();
        return Result.success(this.service.setRoleDataPermission(parameter));
    }

    @Operation(summary = "获取角色的数据权限ID集合")
    @PostMapping("listRoleDataPermissionId")
    public Result<List<Long>> listRoleDataPermissionId(@RequestBody IdParameter roleId) {
        return Result.success(this.service.listRoleDataPermissionId(roleId.getId()));
    }

    @Operation(summary = "根据租户ID查询角色信息")
    @PostMapping("listRoleByTenantId")
    public Result<PageData<SysRolePO>> listRoleByTenantId(@RequestBody @Valid RoleListByTenantIdDTO parameter) {
        // 平台租户忽略查询租户条件
        try (SmartTenantIgnoreData ignore = SmartTenantControl.ignore(SysRolePO.class, null, List.of(SqlCommandType.SELECT))) {
            // 设置分页
            Page<SysRolePO> page = this.doPage(parameter);
            CrudPageHelper.setPage(page);
            // 查询数据
            this.service.lambdaQuery()
                    .eq(SysRolePO::getTenantId, parameter.getTenantId())
                    .list();
            return Result.success(
                    new PageData<>(page.getResult(), page.getTotal())
            );
        }
    }
}
