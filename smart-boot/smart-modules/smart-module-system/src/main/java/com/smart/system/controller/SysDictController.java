package com.smart.system.controller;

import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.IdParameter;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SysDictPO;
import com.smart.system.service.SysDictService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
* sys_dict - 系统字典表 Controller
* @author GCCodeGenerator
* 2022-1-29 10:34:36
*/
@RestController
@RequestMapping("sys/dict")
public class SysDictController extends BaseController<SysDictService, SysDictPO> {

    @Override
    @PostMapping("list")
    @ApiOperation(value = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @ApiOperation(value = "添加修改系统字典表")
    @PostMapping("saveUpdate")
    @Log(value = "添加修改系统字典表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:dict', 'save') or hasPermission('sys:dict', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody SysDictPO model) {
        return Result.success(this.service.saveOrUpdateWithAllUser(model, AuthUtils.getCurrentUserId()));
    }

    @Override
    @ApiOperation(value = "通过ID批量删除系统字典表")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除系统字典表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:dict', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @ApiOperation(value = "通过ID查询")
    @PostMapping("getById")
    public Result<SysDictPO> getById(@RequestBody @Valid IdParameter parameter) {
        return super.getById(parameter.getId());
    }
}
