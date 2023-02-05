package com.smart.system.controller;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SysDictItemPO;
import com.smart.system.pojo.dto.dict.SysDictItemSaveUpdateDTO;
import com.smart.system.service.SysDictItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.BeanUtils;
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
* sys_dict_item - 字典序表 Controller
* @author GCCodeGenerator
* 2022-2-7 10:48:32
*/
@RestController
@RequestMapping("sys/dictItem")
public class SysDictItemController extends BaseController<SysDictItemService, SysDictItemPO> {


    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @Operation(summary = "批量保存/更新字典序表")
    @PostMapping("batchSaveUpdate")
    @Log(value = "批量保存/更新字典序表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:dictItem', 'save') or hasPermission('sys:dictItem', 'update')")
    public Result<Boolean> batchSaveUpdate(@RequestBody List<SysDictItemPO> modelList) {
        return super.batchSaveUpdate(modelList);
    }

    @Operation(summary = "通过ID批量删除字典序表")
    @PostMapping("batchDelete")
    @PreAuthorize("hasPermission('sys:dictItem', 'delete')")
    @Log(value = "通过ID批量删除字典序表", type = LogOperationTypeEnum.DELETE)
    @Override
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        return super.batchDeleteById(idList);
    }

    /**
     * 通过ID获取
     *
     * @param id ID
     * @return 实体类
     */
    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("get")
    public Result<SysDictItemPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}
