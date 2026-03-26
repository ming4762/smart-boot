package com.smart.module.system.controller.microapp;

import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.parameter.SetUseYnParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.module.system.model.micorapp.SysMicroFrontendPO;
import com.smart.module.system.pojo.dto.microapp.SysMicroFrontendSaveUpdateDTO;
import com.smart.module.system.service.microapp.SysMicroFrontendService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
* sys_micro_frontend - 前端微 Controller
* @author SmartCodeGenerator
* 2026年1月20日 15:31:23
*/
@RestController
@RequestMapping("/sys/microApp/microFrontend")
public class SysMicroFrontendController extends BaseController<SysMicroFrontendService, SysMicroFrontendPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询前端微列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改前端微")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改前端微", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:microApp:microFrontend', 'save') or hasPermission('sys:microApp:microFrontend', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SysMicroFrontendSaveUpdateDTO> parameterList) {
        List<SysMicroFrontendPO> modelList = parameterList.stream().map(item -> {
            SysMicroFrontendPO model = new SysMicroFrontendPO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).toList();
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除前端微")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除前端微", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:microApp:microFrontend', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SysMicroFrontendPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 设置启用停用
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    @Operation(summary = "启用停用前端微")
    @PostMapping("setUseYn")
    @Log(value = "启用停用前端微", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:microApp:microFrontend', 'setUseYn')")
    public Result<Boolean> setUseYn(@RequestBody @Valid SetUseYnParameter parameter) {
        return super.setUseYn(parameter);
    }
}