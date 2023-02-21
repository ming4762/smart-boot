package com.smart.system.controller;

import com.smart.commons.core.exception.BusinessException;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.i18n.SystemI18nMessage;
import com.smart.system.model.SysI18nItemPO;
import com.smart.system.pojo.dto.i18n.SysI18nItemSaveUpdateDTO;
import com.smart.system.service.SysI18nItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/11/16
 * @since 1.0.7
 */
@RestController
@RequestMapping("sys/i18nItem")
@Tag(name = "国际化-国际化项管理")
public class SysI18nItemController extends BaseController<SysI18nItemService, SysI18nItemPO> {

    @PostMapping("saveUpdate")
    @Operation(summary = "添加修改国际化项")
    @PreAuthorize("hasPermission('sys:i18n', 'save') or hasPermission('sys:i18n', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody @Valid SysI18nItemSaveUpdateDTO parameter) {
        try {
            SysI18nItemPO model = new SysI18nItemPO();
            BeanUtils.copyProperties(parameter, model);
            return Result.success(this.service.saveOrUpdate(model));
        } catch (DuplicateKeyException e) {
            throw new BusinessException(I18nUtils.get(SystemI18nMessage.I18N_ITEM_DUPLICATE, parameter.getLocale().toLanguageTag()), e);
        }
    }

    @Override
    @Operation(summary = "批量删除国际化项")
    @PostMapping("batchDeleteById")@PreAuthorize("hasPermission('sys:i18n', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        return super.batchDeleteById(idList);
    }

    @Override
    @PostMapping("list")
    @Operation(summary = "查询国际化项")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @PostMapping("getById")
    @Operation(summary = "通过ID查询国际化国际化项")
    public Result<SysI18nItemPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}
