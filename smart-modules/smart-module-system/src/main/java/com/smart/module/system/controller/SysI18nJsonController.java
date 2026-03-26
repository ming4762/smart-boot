package com.smart.module.system.controller;

import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.parameter.SetUseYnParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.i18n.source.ReloadableMessageSource;
import com.smart.module.system.model.SysI18nJsonPO;
import com.smart.module.system.pojo.dto.i18n.SysI18nJsonSaveUpdateDTO;
import com.smart.module.system.service.SysI18nJsonService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
* sys_i18n_json - 国际化信息json Controller
* @author SmartCodeGenerator
* 2025年1月3日 14:39:00
*/
@RestController
@RequestMapping("/sys/i18n/json")
@RequiredArgsConstructor
public class SysI18nJsonController extends BaseController<SysI18nJsonService, SysI18nJsonPO> {

    private final ReloadableMessageSource messageSource;

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改国际化信息json")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改国际化信息json", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:i18n', 'save') or hasPermission('sys:i18n', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SysI18nJsonSaveUpdateDTO> parameterList) {
        List<SysI18nJsonPO> modelList = parameterList.stream().map(item -> {
            SysI18nJsonPO model = new SysI18nJsonPO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).toList();
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除国际化信息json")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除国际化信息json", type = LogOperationTypeEnum.DELETE)
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
    public Result<SysI18nJsonPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 设置启用停用
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    @Operation(summary = "启用停用国际化信息json")
    @PostMapping("setUseYn")
    @Log(value = "启用停用国际化信息json", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:i18n', 'setUseYn')")
    public Result<Boolean> setUseYn(@RequestBody @Valid SetUseYnParameter parameter) {
        return super.setUseYn(parameter);
    }

    @PostMapping("reload")
    @Operation(summary = "刷新国际化信息")
    @PreAuthorize("hasPermission('sys:i18n', 'reload')")
    public Result<Boolean> reload() {
        this.messageSource.reload();
        return Result.success(true);
    }
}