package com.smart.system.controller;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.IdParameter;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SysDictItemPO;
import com.smart.system.model.SysDictPO;
import com.smart.system.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
* sys_dict - 系统字典表 Controller
* @author GCCodeGenerator
* 2022-1-29 10:34:36
*/
@RestController
@RequestMapping("sys/dict")
@Tag(name = "数据字典-字典分组管理")
public class SysDictController extends BaseController<SysDictService, SysDictPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @Operation(summary = "批量保存/更新")
    @PostMapping("batchSaveUpdate")
    @Log(value = "批量保存/更新系统字典表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:dict', 'save') or hasPermission('sys:dict', 'update')")
    public Result<Boolean> batchSaveUpdate(@RequestBody List<SysDictPO> modelList) {
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除系统字典表")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除系统字典表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:dict', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SysDictPO> getById(@RequestBody @Valid IdParameter parameter) {
        return super.getById(parameter.getId());
    }

    @Operation(summary = "通过code查询item")
    @PostMapping("listItemByCode")
    public Result<List<SysDictItemPO>> listItemByCode(@RequestBody String dictCode) {
        return Result.success(this.service.listItemByCode(dictCode));
    }
}
