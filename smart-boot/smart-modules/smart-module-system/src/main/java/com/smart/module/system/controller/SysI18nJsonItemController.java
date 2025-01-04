package com.smart.module.system.controller;

import com.smart.framework.commons.core.exception.BusinessException;
import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.parameter.SetUseYnParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.module.system.model.SysI18nJsonItemPO;
import com.smart.module.system.pojo.dto.SysI18nJsonItemSaveUpdateDTO;
import com.smart.module.system.service.SysI18nJsonItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
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
* sys_i18n_json_item - 国际化信息json项 Controller
* @author SmartCodeGenerator
* 2025年1月3日 19:22:50
*/
@RestController
@RequestMapping("/sys/i18n/jsonItem/")
public class SysI18nJsonItemController extends BaseController<SysI18nJsonItemService, SysI18nJsonItemPO> {
    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改国际化信息json项")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改国际化信息json项", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:i18n', 'save') or hasPermission('sys:i18n', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SysI18nJsonItemSaveUpdateDTO> parameterList) {
        List<SysI18nJsonItemPO> modelList = parameterList.stream().map(item -> {
            SysI18nJsonItemPO model = new SysI18nJsonItemPO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).toList();
        try {
            return super.batchSaveUpdate(modelList);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("国际化信息已存在，请勿重复添加");
        }
    }

    @Override
    @Operation(summary = "通过ID批量删除国际化信息json项")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除国际化信息json项", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:i18n', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SysI18nJsonItemPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 设置启用停用
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    @Operation(summary = "启用停用国际化信息json项")
    @PostMapping("setUseYn")
    @Log(value = "启用停用国际化信息json项", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:i18n', 'setUseYn')")
    public Result<Boolean> setUseYn(@RequestBody @Valid SetUseYnParameter parameter) {
        return super.setUseYn(parameter);
    }
}