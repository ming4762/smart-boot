package com.smart.module.system.controller;

import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.module.system.model.SysChangeLogPO;
import com.smart.module.system.pojo.dto.changelog.SysChangeLogSaveUpdateDTO;
import com.smart.module.system.service.SysChangeLogService;
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
* sys_change_log - 系统变更日志 Controller
* @author SmartCodeGenerator
* 2025年5月5日 19:12:09
*/
@RestController
@RequestMapping("/sys/changeLog")
public class SysChangeLogController extends BaseController<SysChangeLogService, SysChangeLogPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改系统变更日志")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改系统变更日志", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:changeLog', 'save') or hasPermission('sys:changeLog', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SysChangeLogSaveUpdateDTO> parameterList) {
        List<SysChangeLogPO> modelList = parameterList.stream().map(item -> {
            SysChangeLogPO model = new SysChangeLogPO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).toList();
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除系统变更日志")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除系统变更日志", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:changeLog', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SysChangeLogPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}