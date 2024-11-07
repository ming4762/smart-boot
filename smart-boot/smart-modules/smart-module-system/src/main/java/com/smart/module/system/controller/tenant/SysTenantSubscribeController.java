package com.smart.module.system.controller.tenant;

import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.parameter.SetUseYnParameter;
import com.smart.module.system.constants.SystemConstantEnum;
import com.smart.module.system.model.tenant.SysTenantSubscribePO;
import com.smart.module.system.pojo.dto.tenant.SysTenantSubscribeListDTO;
import com.smart.module.system.pojo.dto.tenant.SysTenantSubscribeSaveUpdateDTO;
import com.smart.module.system.service.tenant.SysTenantSubscribeService;
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
* sys_tenant_subscribe - 租户套餐订阅表 Controller
* @author SmartCodeGenerator
* 2024年4月6日 下午6:41:30
*/
@RestController
@RequestMapping("/sys/tenant/subscribe/")
public class SysTenantSubscribeController extends BaseController<SysTenantSubscribeService, SysTenantSubscribePO> {

    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull @Valid SysTenantSubscribeListDTO parameter) {
        return super.list(parameter);
    }

    @PostMapping("listWithPackage")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> listWithPackage(@RequestBody @NonNull @Valid SysTenantSubscribeListDTO parameter) {
        parameter.getParameter().put(SystemConstantEnum.TENANT_SUBSCRIBE_LIST_WITH_PACKAGE.name(), Boolean.TRUE);
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改租户套餐订阅表")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改租户套餐订阅表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:tenant:manager:subscribe', 'addUpdate')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SysTenantSubscribeSaveUpdateDTO> parameterList) {
        List<SysTenantSubscribePO> modelList = parameterList.stream().map(item -> {
            SysTenantSubscribePO model = new SysTenantSubscribePO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).toList();
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除租户套餐订阅表")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除租户套餐订阅表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:tenant:manager:subscribe', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SysTenantSubscribePO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 设置启用停用
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    @Operation(summary = "启用停用租户套餐订阅表")
    @PostMapping("setUseYn")
    @Log(value = "启用停用租户套餐订阅表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:tenant:manager:subscribe', 'setUseYn')")
    public Result<Boolean> setUseYn(@RequestBody @Valid SetUseYnParameter parameter) {
        return super.setUseYn(parameter);
    }

}
