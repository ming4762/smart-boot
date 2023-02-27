package com.smart.system.controller;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SysParameterPO;
import com.smart.system.pojo.dto.parameter.SysParameterSaveUpdateDTO;
import com.smart.system.service.SysParameterService;
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
* sys_parameter - 系统参数表 Controller
* @author SmartCodeGenerator
* 2023-2-27
*/
@RestController
@RequestMapping("sys/parameter")
public class SysParameterController extends BaseController<SysParameterService, SysParameterPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('sys:parameter', 'query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改系统参数表")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改系统参数表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:parameter', 'save') or hasPermission('sys:parameter', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SysParameterSaveUpdateDTO> parameterList) {
      	List<SysParameterPO> modelList = parameterList.stream().map(item -> {
            SysParameterPO model = new SysParameterPO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).collect(Collectors.toList());
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除系统参数表")
    @PostMapping("batchDeleteById")
    @PreAuthorize("hasPermission('sys:parameter', 'delete')")
    @Log(value = "通过ID批量删除系统参数表", type = LogOperationTypeEnum.DELETE)
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    @PreAuthorize("hasPermission('sys:parameter', 'query')")
    public Result<SysParameterPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}