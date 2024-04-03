package com.smart.system.controller.tenant;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.parameter.SetUseYnParameter;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.tenant.SysTenantPackagePO;
import com.smart.system.pojo.dto.tenant.SysTenantPackageSaveUpdateDTO;
import com.smart.system.service.tenant.SysTenantPackageService;
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
* sys_tenant_package - 租户产品套餐 Controller
* @author SmartCodeGenerator
* 2024年4月2日 下午3:02:14
*/
@RestController
@RequestMapping("/sys/tenant/package/")
public class SysTenantPackageController extends BaseController<SysTenantPackageService, SysTenantPackagePO> {


    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改租户产品套餐")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改租户产品套餐", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:tenant:package', 'save') or hasPermission('sys:tenant:package', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SysTenantPackageSaveUpdateDTO> parameterList) {
        List<SysTenantPackagePO> modelList = parameterList.stream().map(item -> {
            SysTenantPackagePO model = new SysTenantPackagePO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).toList();
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除租户产品套餐")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除租户产品套餐", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:tenant:package', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SysTenantPackagePO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 设置启用停用
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    @Operation(summary = "启用停用租户产品套餐")
    @PostMapping("setUseYn")
    @Log(value = "启用停用租户产品套餐", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:tenant:package', 'setUseYn')")
    public Result<Boolean> setUseYn(@RequestBody @Valid SetUseYnParameter parameter) {
        return super.setUseYn(parameter);
    }
}