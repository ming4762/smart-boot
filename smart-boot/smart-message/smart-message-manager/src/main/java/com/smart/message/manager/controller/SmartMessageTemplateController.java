package com.smart.message.manager.controller;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.message.manager.model.SmartMessageTemplatePO;
import com.smart.message.manager.pojo.paramteter.SmartMessageTemplateSaveUpdateParameter;
import com.smart.message.manager.service.SmartMessageTemplateService;
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
import java.util.stream.Collectors;

/**
* smart_message_template - 消息模板表 Controller
* @author SmartCodeGenerator
* 2023年6月28日 下午2:06:28
*/
@RestController
@RequestMapping("/smart/message/template")
public class SmartMessageTemplateController extends BaseController<SmartMessageTemplateService, SmartMessageTemplatePO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改消息模板表")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改消息模板表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('smart:message:template', 'save') or hasPermission('smart:message:template', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SmartMessageTemplateSaveUpdateParameter> parameterList) {
      	List<SmartMessageTemplatePO> modelList = parameterList.stream().map(item -> {
            SmartMessageTemplatePO model = new SmartMessageTemplatePO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).collect(Collectors.toList());
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除消息模板表")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除消息模板表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('smart:message:template', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SmartMessageTemplatePO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}