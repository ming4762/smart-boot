package com.smart.module.system.controller.tenant;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.parameter.SetUseYnParameter;
import com.smart.framework.crud.query.IdParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.module.system.model.tenant.SysTenantPackageFunctionPO;
import com.smart.module.system.model.tenant.SysTenantPackagePO;
import com.smart.module.system.pojo.dto.tenant.SysTenantPackageSaveFunctionDTO;
import com.smart.module.system.pojo.dto.tenant.SysTenantPackageSaveUpdateDTO;
import com.smart.module.system.service.tenant.SysTenantPackageFunctionService;
import com.smart.module.system.service.tenant.SysTenantPackageService;
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


    private final SysTenantPackageFunctionService sysTenantPackageFunctionService;

    public SysTenantPackageController(SysTenantPackageFunctionService sysTenantPackageFunctionService) {
        this.sysTenantPackageFunctionService = sysTenantPackageFunctionService;
    }

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

    /**
     * 获取套餐包对应的功能ID集合
     * @param parameter 角色ID
     * @return 功能ID集合
     */
    @Operation(summary = "获取套餐包对应的功能ID集合")
    @PostMapping("listFunctionId")
    public Result<List<Long>> listFunctionId(@RequestBody IdParameter parameter) {
        return Result.success(
                this.sysTenantPackageFunctionService.listPackageFunction(
                                new QueryWrapper<SysTenantPackageFunctionPO>()
                                        .eq("B.has_child", 0).lambda()
                                        .eq(SysTenantPackageFunctionPO :: getTenantPackageId, parameter.getId())
                                        .eq(SysTenantPackageFunctionPO::getHalfYn, Boolean.FALSE)
                        )
                        .stream().map(SysTenantPackageFunctionPO :: getFunctionId)
                        .toList()
        );
    }

    @Operation(summary = "保存租户套餐功能")
    @PostMapping("savePackageFunction")
    @PreAuthorize("hasPermission('sys:tenant:package', 'savePackageFunction')")
    public Result<Boolean> savePackageFunction(@RequestBody @Valid SysTenantPackageSaveFunctionDTO parameter) {
        return Result.success(this.service.savePackageFunction(parameter));
    }
}