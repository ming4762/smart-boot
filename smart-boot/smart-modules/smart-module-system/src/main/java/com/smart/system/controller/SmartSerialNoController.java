package com.smart.system.controller;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SmartSerialNoPO;
import com.smart.system.pojo.parameter.serial.SmartSerialNoSaveUpdateParameter;
import com.smart.system.service.SmartSerialNoService;
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
* smart_serial_no - 业务编码表 Controller
* @author SmartCodeGenerator
* 2023年6月2日 上午9:56:44
*/
@RestController
@RequestMapping("/smart/tool/serial/")
public class SmartSerialNoController extends BaseController<SmartSerialNoService, SmartSerialNoPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('sys:tool.serial', 'query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改业务编码表")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改业务编码表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:tool.serial', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SmartSerialNoSaveUpdateParameter> parameterList) {
      	List<SmartSerialNoPO> modelList = parameterList.stream().map(item -> {
            SmartSerialNoPO model = new SmartSerialNoPO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).toList();
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除业务编码表")
    @PostMapping("batchDeleteById")
    @PreAuthorize("hasPermission('sys:tool.serial', 'delete')")
    @Log(value = "通过ID批量删除业务编码表", type = LogOperationTypeEnum.DELETE)
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    @PreAuthorize("hasPermission('sys:tool.serial', 'query')")
    public Result<SmartSerialNoPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}