package com.smart.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.annotation.NonUrlCheck;
import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SysRoleFunctionPO;
import com.smart.system.model.SysRolePO;
import com.smart.system.pojo.dto.role.RoleMenuSaveDTO;
import com.smart.system.pojo.dto.role.RoleSetUserDTO;
import com.smart.system.service.SysRoleFunctionService;
import com.smart.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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

    @Override
    @Operation(summary = "添加修改角色")
    @PostMapping("saveUpdate")
    @Log(value = "添加修改角色", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:role', 'save') or hasPermission('sys:role', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody SysRolePO model) {
        return Result.success(this.service.saveOrUpdate(model));
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
     * @param roleId 角色ID
     * @return 功能ID集合
     */
    @Operation(summary = "获取角色对应的功能ID集合")
    @PostMapping("listFunctionId")
    public Result<List<Long>> listFunctionId(@RequestBody Long roleId) {
        return Result.success(
                this.sysRoleFunctionService.list(
                        new QueryWrapper<SysRoleFunctionPO>().lambda()
                        .select(SysRoleFunctionPO :: getFunctionId)
                        .eq(SysRoleFunctionPO :: getRoleId, roleId)
                ).stream().map(SysRoleFunctionPO :: getFunctionId)
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
}
