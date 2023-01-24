package com.smart.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.IdParameter;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SysSystemPO;
import com.smart.system.model.SysSystemUserPO;
import com.smart.system.pojo.dto.system.SysSystemSaveUpdateDTO;
import com.smart.system.pojo.dto.system.SystemSetUserDTO;
import com.smart.system.service.SysSystemService;
import com.smart.system.service.SysSystemUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
* sys_system - 系统管理表 Controller
* @author SmartCodeGenerator
* 2023-1-22 20:00:47
*/
@RestController
@RequestMapping("/sys/system")
public class SysSystemController extends BaseController<SysSystemService, SysSystemPO> {

    private final SysSystemUserService sysSystemUserService;

    public SysSystemController(SysSystemUserService sysSystemUserService) {
        this.sysSystemUserService = sysSystemUserService;
    }

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('sys:system:query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "添加修改系统管理表")
    @PostMapping("saveUpdate")
    @Log(value = "添加修改系统管理表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:system:add') or hasPermission('sys:system:edit')")
    public Result<Boolean> saveUpdate(@RequestBody @Valid SysSystemSaveUpdateDTO parameter) {
      	SysSystemPO model = new SysSystemPO();
      	BeanUtils.copyProperties(parameter, model);
        return super.saveUpdate(model);
    }

    @Override
    @Operation(summary = "通过ID批量删除系统管理表")
    @PostMapping("batchDeleteById")
    @PreAuthorize("hasPermission('sys:system:delete')")
    @Log(value = "通过ID批量删除系统管理表", type = LogOperationTypeEnum.DELETE)
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    @PreAuthorize("hasPermission('sys:system:query')")
    public Result<SysSystemPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @PostMapping("setUser")
    @PreAuthorize("hasPermission('sys:system:setUser')")
    @Operation(summary = "设置关联用户")
    public Result<Boolean> setUser(@RequestBody SystemSetUserDTO parameter) {
        return Result.success(this.service.setUser(parameter));
    }

    @PostMapping("getRelatedUserId")
    @PreAuthorize("hasPermission('sys:system:query')")
    @Operation(summary = "获取关联用户ID")
    public Result<List<Long>> getRelatedUserId(@RequestBody @Valid IdParameter parameter) {
        return Result.success(
                this.sysSystemUserService.list(
                        new QueryWrapper<SysSystemUserPO>().lambda()
                                .select(SysSystemUserPO::getUserId)
                                .eq(SysSystemUserPO::getSystemId, parameter.getId())
                ).stream().map(SysSystemUserPO::getUserId).collect(Collectors.toList())
        );
    }
}