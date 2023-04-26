package com.smart.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.exception.BusinessException;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.i18n.source.ReloadableMessageSource;
import com.smart.system.i18n.SystemI18nMessage;
import com.smart.system.model.SysI18nGroupPO;
import com.smart.system.model.SysI18nPO;
import com.smart.system.pojo.dto.i18n.SysI18nGroupSaveUpdateDTO;
import com.smart.system.pojo.dto.i18n.SysI18nSaveUpdateDTO;
import com.smart.system.service.SysI18nGroupService;
import com.smart.system.service.SysI18nService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
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
 * @author ShiZhongMing
 * 2021/11/12
 * @since 1.0.7
 */
@RestController
@RequestMapping("sys/i18n")
@Slf4j
@Tag(name = "国际化-国际化分组&国际化管理")
public class SysI18nController extends BaseController<SysI18nService, SysI18nPO> {

    private final SysI18nGroupService sysI18nGroupService;

    private final ReloadableMessageSource messageSource;

    public SysI18nController(SysI18nGroupService sysI18nGroupService, ReloadableMessageSource messageSource) {
        this.sysI18nGroupService = sysI18nGroupService;
        this.messageSource = messageSource;
    }

    @PostMapping("listGroup")
    @Operation(summary = "查询国际化组信息")
    public Result<List<SysI18nGroupPO>> listGroup() {
        return Result.success(
                this.sysI18nGroupService.list(
                        new QueryWrapper<SysI18nGroupPO>().lambda()
                        .orderByAsc(SysI18nGroupPO :: getSeq, SysI18nGroupPO :: getCreateTime)
                )
        );
    }

    @PostMapping("saveOrUpdateGroup")
    @Operation(summary = "保存/更新国际化组")
    @Log(value = "保存/更新国际化组", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:i18n', 'save') or hasPermission('sys:i18n', 'update')")
    public Result<Boolean> saveOrUpdateGroup(@RequestBody @Valid SysI18nGroupSaveUpdateDTO parameter) {
        SysI18nGroupPO group = new SysI18nGroupPO();
        BeanUtils.copyProperties(parameter, group);
        return Result.success(this.sysI18nGroupService.saveOrUpdate(group));
    }

    @PostMapping("deleteGroup")
    @Operation(summary = "删除国际化组")
    @Log(value = "删除国际化组", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:i18n', 'delete')")
    public Result<Boolean> deleteGroup(@RequestBody List<Long> idList) {
        return Result.success(this.sysI18nGroupService.removeByIds(idList));
    }

    @PostMapping("getGroupById")
    @Operation(summary = "通过ID查询国际化组")
    public Result<SysI18nGroupPO> getGroupById(@RequestBody Long groupId) {
        return Result.success(this.sysI18nGroupService.getById(groupId));
    }

    @Override
    @PostMapping("list")
    @Operation(summary = "查询国际化信息")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @PostMapping("getById")
    @Operation(summary = "通过ID查询国际化信息")
    @Override
    public Result<SysI18nPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @PostMapping("saveUpdate")
    @Operation(summary = "保存/更新国际化信息")
    @Log(value = "保存/更新国际化信息", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:i18n', 'save') or hasPermission('sys:i18n', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody @Valid SysI18nSaveUpdateDTO parameter) {
        try {
            SysI18nPO model = new SysI18nPO();
            BeanUtils.copyProperties(parameter, model);
            return Result.success(this.service.saveOrUpdate(model));
        } catch (DuplicateKeyException e) {
            throw new BusinessException(I18nUtils.get(SystemI18nMessage.I18N_DUPLICATE, (Object) parameter.getI18nCode()), e);
        }
    }

    @PostMapping("batchDeleteById")
    @Operation(summary = "删除国际化信息")
    @Log(value = "删除国际化信息", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:i18n', 'delete')")
    @Override
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        return super.batchDeleteById(idList);
    }

    @PostMapping("reload")
    @Operation(summary = "刷新国际化信息")
    @PreAuthorize("hasPermission('sys:i18n', 'reload')")
    public Result<Boolean> reload() {
        this.messageSource.reload();
        return Result.success(true);
    }
}
