package com.smart.module.system.controller;

import com.smart.framework.auth.common.annotation.NonUrlCheck;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.query.IdParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.module.system.model.SysFunctionPO;
import com.smart.module.system.pojo.parameter.function.SysFunctionSaveUpdateParameter;
import com.smart.module.system.pojo.vo.function.SysFunctionVO;
import com.smart.module.system.service.SysFunctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 功能controller
 * @author 史仲明
 * 2020/1/27 12:16 下午
 */
@RestController
@RequestMapping("sys/function")
@Tag(name = "系统功能")
@NonUrlCheck
public class SysFunctionController extends BaseController<SysFunctionService, SysFunctionPO> {


    @Override
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除功能", type = LogOperationTypeEnum.DELETE)
    @Operation(summary = "通过ID批量删除功能", method = "POST")
    @PreAuthorize("hasPermission('sys:function', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return Result.success(this.service.removeByIds(idList));
    }

    @PostMapping("getById")
    @Operation(summary = "通过ID获取", method = "POST")
    public Result<SysFunctionVO> getById(@RequestBody Long id) {
        return Result.success(this.service.getUserAndParentById(id));
    }

    @PostMapping("saveUpdate")
    @Operation(summary = "添加修改功能")
    @Log(value = "添加保存功能", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:function', 'save') or hasPermission('sys:function', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody @Valid SysFunctionSaveUpdateParameter parameter) {
        return Result.success(this.service.saveUpdate(parameter));
    }

    @Override
    @PostMapping("list")
    @Operation(summary = "查询功能列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('sys:function', 'query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @PostMapping("listTenantFunction")
    @Operation(summary = "查询租户对应的功能")
    public Result<List<SysFunctionPO>> listTenantFunction(@RequestBody IdParameter parameter) {
        return Result.success(this.service.listTenantFunction(Objects.requireNonNullElseGet(parameter.getId(), AuthUtils::getNonNullCurrentTenantId)));
    }
}
