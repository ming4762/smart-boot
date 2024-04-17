package com.smart.system.controller;

import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.constants.SystemConstantEnum;
import com.smart.system.model.SysCategoryPO;
import com.smart.system.pojo.dto.category.SysCategorySaveUpdateDTO;
import com.smart.system.pojo.vo.category.SysCategoryGetVO;
import com.smart.system.service.SysCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
* sys_category - 分类字段 Controller
* @author SmartCodeGenerator
* 2023-1-21 21:32:15
*/
@RestController
@RequestMapping("/sys/category")
@Tag(name = "分类字典")
public class SysCategoryController extends BaseController<SysCategoryService, SysCategoryPO> {

    @PostMapping("listFilterTenant")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> listFilterTenant(@RequestBody @NonNull PageSortQuery parameter) {
        parameter.getParameter().put(SystemConstantEnum.LIST_FILTER_TENANT, Boolean.TRUE);
        parameter.getParameter().put(SystemConstantEnum.LIST_WITH_TENANT, Boolean.TRUE);
        return super.list(parameter);
    }

    @Operation(summary = "添加修改分类字段")
    @PostMapping("saveUpdate")
    @Log(value = "添加修改分类字段", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:category', 'add') or hasPermission('sys:category', 'edit')")
    public Result<Boolean> saveUpdate(@RequestBody @Valid SysCategorySaveUpdateDTO parameter) {
        if (Boolean.TRUE.equals(parameter.getTenantCommonYn()) && !AuthUtils.isPlatformTenant()) {
            throw new AccessDeniedException("非平台管理租户无权添加平台通用字典");
        }
        SysCategoryPO model = new SysCategoryPO();
        BeanUtils.copyProperties(parameter, model);
        if (Boolean.FALSE.equals(parameter.getTenantCommonYn()) && AuthUtils.isPlatformTenant()) {
            model.setTenantId(AuthUtils.getNonNullCurrentTenantId());
        }
        return super.saveUpdate(model);
    }

    @Override
    @Operation(summary = "通过ID批量删除分类字段")
    @PostMapping("batchDeleteById")
    @PreAuthorize("hasPermission('sys:category', 'delete')")
    @Log(value = "通过ID批量删除分类字段", type = LogOperationTypeEnum.DELETE)
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    @PreAuthorize("hasPermission('sys:category', 'query')")
    public Result<SysCategoryPO> getById(@RequestBody Serializable id) {
        SysCategoryPO data = this.service.getById(id);
        if (data != null) {
            SysCategoryGetVO vo = new SysCategoryGetVO();
            BeanUtils.copyProperties(data, vo);
            vo.setParent(this.service.getById(vo.getParentId()));
            return Result.success(vo);
        }
        return Result.success();
    }
}