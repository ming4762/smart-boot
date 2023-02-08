package com.smart.system.controller;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SysDeptPO;
import com.smart.system.pojo.dto.dept.SysDeptSaveUpdateDTO;
import com.smart.system.service.SysDeptService;
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
* sys_dept - 部门表 Controller
* @author GCCodeGenerator
* 2022年10月13日 上午10:24:21
*/
@RestController
@RequestMapping("sys/dept")
public class SysDeptController extends BaseController<SysDeptService, SysDeptPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('sys:dept', 'query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "添加修改部门表")
    @PostMapping("saveUpdate")
    @Log(value = "添加修改部门表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:dept', 'save') or hasPermission('sys:dept', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody @Valid SysDeptSaveUpdateDTO parameter) {
      	SysDeptPO model = new SysDeptPO();
      	BeanUtils.copyProperties(parameter, model);
        return Result.success(
                this.service.saveOrUpdate(model)
        );
    }

    @Operation(summary = "批量添加修改部门表")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改部门表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:dept', 'save') or hasPermission('sys:dept', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SysDeptSaveUpdateDTO> parameter) {
        List<SysDeptPO> list = parameter.stream().map(item -> {
            SysDeptPO dept = new SysDeptPO();
            BeanUtils.copyProperties(item, dept);
            return dept;
        }).collect(Collectors.toList());
        return Result.success(this.service.saveOrUpdateBatch(list));
    }

    @Override
    @Operation(summary = "通过ID批量删除部门表")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除部门表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:dept', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    @PreAuthorize("hasPermission('sys:dept', 'query')")
    public Result<SysDeptPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}
