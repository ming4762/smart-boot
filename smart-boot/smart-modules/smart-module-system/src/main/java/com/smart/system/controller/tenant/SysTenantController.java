package com.smart.system.controller.tenant;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.tenant.SysTenantPO;
import com.smart.system.pojo.dto.tenant.SysTenantSaveUpdateDTO;
import com.smart.system.service.tenant.SysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
* sys_tenant - 租户表 Controller
* @author SmartCodeGenerator
* 2023-2-26 12:18:21
*/
@RestController
@RequestMapping("sys/tenant")
public class SysTenantController extends BaseController<SysTenantService, SysTenantPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('sys:tenant', 'query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改租户表")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改租户表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:tenant', 'save') or hasPermission('sys:tenant', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SysTenantSaveUpdateDTO> parameterList) {
      	List<SysTenantPO> modelList = parameterList.stream().map(item -> {
            SysTenantPO model = new SysTenantPO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).toList();
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除租户表")
    @PostMapping("batchDeleteById")
    @PreAuthorize("hasPermission('sys:tenant', 'delete')")
    @Log(value = "通过ID批量删除租户表", type = LogOperationTypeEnum.DELETE)
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    @PreAuthorize("hasPermission('sys:tenant', 'query')")
    public Result<SysTenantPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}