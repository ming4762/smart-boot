package com.smart.module.system.controller.tenant;

import com.github.pagehelper.Page;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.dto.common.LabelValueData;
import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.PageData;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.commons.core.utils.EnumUtils;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.parameter.SetUseYnParameter;
import com.smart.framework.crud.query.IdParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.utils.CrudPageHelper;
import com.smart.module.system.constants.SysTenantIsolationStrategyEnum;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.model.tenant.SysTenantPO;
import com.smart.module.system.model.tenant.SysTenantPackagePO;
import com.smart.module.system.pojo.dbo.tenant.SysTenantUserListDO;
import com.smart.module.system.pojo.dto.tenant.*;
import com.smart.module.system.service.SysUserAccountService;
import com.smart.module.system.service.tenant.SysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
 * sys_tenant - 租户表 Controller
 * @author SmartCodeGenerator
 * 2024年3月29日 下午1:40:04
 */
@RestController
@RequestMapping("/sys/tenant/manager")
@RequiredArgsConstructor
public class SysTenantController extends BaseController<SysTenantService, SysTenantPO> {

    private final SysUserAccountService sysUserAccountService;

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('sys:tenant:manager', 'query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "添加修改租户表")
    @PostMapping("saveUpdate")
    @Log(value = "添加修改租户表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:tenant:manager', 'save') or hasPermission('sys:tenant:manager', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody @Valid SysTenantSaveUpdateDTO parameter) {
        SysTenantPO model = new SysTenantPO();
        BeanUtils.copyProperties(parameter, model);
        return super.saveUpdate(model);
    }

    @Override
    @Operation(summary = "通过ID批量删除租户表")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除租户表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:tenant:manager', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SysTenantPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 设置启用停用
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    @Operation(summary = "启用停用租户表")
    @PostMapping("setUseYn")
    @Log(value = "启用停用租户表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:tenant:manager', 'setUseYn')")
    public Result<Boolean> setUseYn(@RequestBody @Valid SetUseYnParameter parameter) {
        return super.setUseYn(parameter);
    }

    /**
     * 查询隔离策略
     * @return 隔离策略
     */
    @Operation(summary = "查询隔离策略")
    @PostMapping("listIsolationStrategy")
    public Result<List<LabelValueData<SysTenantIsolationStrategyEnum>>> listIsolationStrategy() {
        return Result.success(EnumUtils.convertLabelValue(SysTenantIsolationStrategyEnum.class));
    }

    @Operation(summary = "查询租户对应用户")
    @PostMapping("listTenantUser")
    public Result<PageData<SysTenantUserListDO>> listTenantUser(@RequestBody SysTenantUserListDTO parameter) {
        Page<SysTenantUserListDO> page = this.doPage(parameter);
        CrudPageHelper.setPage(page);
        this.service.listTenantUser(parameter);
        return Result.success(new PageData<>(page.getResult(), page.getTotal()));
    }

    @Operation(summary = "查询未绑定租户的用户")
    @PostMapping("listNoBindUser")
    public Result<PageData<SysUserPO>> listNoBindUser(@RequestBody @Valid SysTenantListNoBindUserDTO parameter) {
        Page<SysUserPO> page = this.doPage(parameter);
        CrudPageHelper.setPage(page);
        this.service.listNoBindUser(parameter);
        return Result.success(new PageData<>(page.getResult(), page.getTotal()));
    }

    @Operation(summary = "绑定用户")
    @PostMapping("bindTenantUser")
    @PreAuthorize("hasPermission('sys:tenant:manager', 'bindUser')")
    public Result<Boolean> bindTenantUser(@RequestBody @Valid SysTenantBindUserDTO parameter) {
        return Result.success(this.service.bindTenantUser(parameter));
    }

    @Operation(summary = "解绑用户")
    @PostMapping("removeBindUser")
    @PreAuthorize("hasPermission('sys:tenant:manager', 'bindUser')")
    public Result<Boolean> removeBindUser(@RequestBody @Valid SysTenantRemoveBindUserDTO parameter) {
        return Result.success(this.service.removeBindUser(parameter));
    }

    @Operation(summary = "根据租户ID查询没有绑定的套餐")
    @PostMapping("listNoBindPackageByTenantId")
    public Result<List<SysTenantPackagePO>> listNoBindPackageByTenantId(@RequestBody IdParameter parameter) {
        return Result.success(this.service.listNoBindPackageByTenantId(parameter));
    }

    @Operation(summary = "查询当前用户的租户")
    @PostMapping("listCurrentUserTenant")
    public Result<List<SysTenantPO>> listCurrentUserTenant() {
        return Result.success(this.service.listTenantByUserId(AuthUtils.getNonNullCurrentUserId()));
    }

    @Operation(summary = "创建用户对应租户账户")
    @PostMapping("createTenantUserAccount")
    public Result<Boolean> createTenantUserAccount(@RequestBody @Valid SysCreateTenantUserAccountDTO parameter) {
        // 验证用户是否是平台账户
        if (!AuthUtils.isPlatformTenant()) {
            throw new AccessDeniedException("非平台管理租户无权限创建其他租户账户");
        }
        return Result.success(this.sysUserAccountService.createAccount(parameter.getTenantId(), parameter.getUserIdList()));
    }

    @Operation(summary = "查询租户权限")
    @PostMapping("listTenantNoAuth")
    public Result<List<SysTenantPO>> listTenantNoAuth() {
        if (!AuthUtils.isPlatformTenant()) {
            throw new AccessDeniedException("非平台管理租户无权限查看租户列表");
        }
        return Result.success(
                this.service.lambdaQuery()
                        .eq(SysTenantPO::getUseYn, Boolean.TRUE)
                        .orderByAsc(SysTenantPO::getSeq)
                        .list()
        );
    }
}