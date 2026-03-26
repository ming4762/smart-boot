package com.smart.module.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.exception.BusinessException;
import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.module.system.constants.SystemConstantEnum;
import com.smart.module.system.model.SysParameterTenantPO;
import com.smart.module.system.pojo.dto.parameter.SysParameterTenantSaveUpdateDTO;
import com.smart.module.system.service.SysParameterTenantService;
import io.swagger.v3.oas.annotations.Operation;
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
* sys_parameter_tenant - 系统参数租户表 Controller
* @author SmartCodeGenerator
* 2025年9月4日 19:47:33
*/
@RestController
@RequestMapping("/sys/parameterTenant")
public class SysParameterTenantController extends BaseController<SysParameterTenantService, SysParameterTenantPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询系统参数租户表列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    /**
     * 列表查询（包含租户）
     * @param parameter 参数
     * @return 查询结果
     */
    @PostMapping("listWithTenant")
    @Operation(summary = "查询系统参数租户表列表（包含租户）")
    public Result<Object> listWithTenant(@RequestBody @NonNull PageSortQuery parameter) {
        parameter.getParameter().put(SystemConstantEnum.LIST_WITH_TENANT, Boolean.TRUE);
        return super.list(parameter);
    }

    @PostMapping("save")
    @Operation(summary = "添加系统参数租户表")
    @Log(value = "添加系统参数租户表", type = LogOperationTypeEnum.ADD)
    @PreAuthorize("hasPermission('sys:parameter', 'save')")
    public Result<Boolean> save(@RequestBody SysParameterTenantSaveUpdateDTO parameter) {
        // 参数校验
        this.validateParameter(parameter);
        // 2、校验当前租户是否重复
        SysParameterTenantPO model = new SysParameterTenantPO();
        BeanUtil.copyProperties(parameter, model);
        // 通用参数设置租户ID
        if (Boolean.TRUE.equals(parameter.getCommonYn())) {
            model.setTenantId(SysParameterTenantPO.COMMON_PARAMETER_TENANT_ID);
        } else {
            model.setTenantId(AuthUtils.getCurrentTenantId());
        }
        return Result.success(this.service.save(model));
    }

    /**
     * 校验参数
     * @param parameter 系统参数租户表
     */
    private void validateParameter(SysParameterTenantSaveUpdateDTO parameter) {
        // 1、校验通用参数是否重复
        if (Boolean.TRUE.equals(parameter.getCommonYn())) {
            Long commonCount = this.service.lambdaQuery()
                    .eq(SysParameterTenantPO::getParameterId, parameter.getParameterId())
                    .eq(SysParameterTenantPO::getTenantId, SysParameterTenantPO.COMMON_PARAMETER_TENANT_ID)
                    .count();
            if (commonCount > 0) {
                throw new BusinessException("通用参数已存在，不能重复维护");
            }
            return;
        }
        // 2、校验当前租户是否重复
        Long tenantCount = this.service.lambdaQuery()
                .eq(SysParameterTenantPO::getParameterId, parameter.getParameterId())
                .eq(SysParameterTenantPO::getTenantId, AuthUtils.getCurrentTenantId())
                .count();
        if (tenantCount > 0) {
            throw new BusinessException("当前租户参数已存在，不能重复维护");
        }
    }

    @PostMapping("update")
    @Operation(summary = "更新系统参数租户表")
    @Log(value = "更新系统参数租户表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:parameter', 'update')")
    public Result<Boolean> update(@RequestBody SysParameterTenantSaveUpdateDTO parameter) {
        SysParameterTenantPO model = new SysParameterTenantPO();
        BeanUtil.copyProperties(parameter, model);
        return super.update(model);
    }

    @Override
    @Operation(summary = "通过ID批量删除系统参数租户表")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除系统参数租户表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:parameter', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SysParameterTenantPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}