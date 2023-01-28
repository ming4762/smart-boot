package com.smart.system.controller;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SmartAuthLicensePO;
import com.smart.system.pojo.dto.license.SmartAuthLicenseSaveUpdateDTO;
import com.smart.system.service.SmartAuthLicenseService;
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
* smart_auth_license - 许可证管理 Controller
* @author SmartCodeGenerator
* 2022-12-17 14:31:50
*/
@RestController
@RequestMapping("/smart/license")
public class SmartAuthLicenseController extends BaseController<SmartAuthLicenseService, SmartAuthLicensePO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('sys:license', 'query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "添加修改许可证管理")
    @PostMapping("saveUpdate")
    @PreAuthorize("hasPermission('sys:license', 'save') or hasPermission('sys:license', 'update')")
    @Log(value = "添加修改许可证管理", type = LogOperationTypeEnum.UPDATE)
    public Result<Boolean> saveUpdate(@RequestBody @Valid SmartAuthLicenseSaveUpdateDTO parameter) {
      	SmartAuthLicensePO model = new SmartAuthLicensePO();
      	BeanUtils.copyProperties(parameter, model);
        return super.saveUpdate(model);
    }

    @Override
    @Operation(summary = "通过ID批量删除许可证管理")
    @PostMapping("batchDeleteById")
    @PreAuthorize("hasPermission('sys:license', 'delete')")
    @Log(value = "通过ID批量删除许可证管理", type = LogOperationTypeEnum.DELETE)
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    @PreAuthorize("hasPermission('sys:license', 'query')")
    public Result<SmartAuthLicensePO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @PostMapping("generator")
    @PreAuthorize("hasPermission('sys:license', 'generator')")
    @Operation(summary = "生成license")
    public Result<Boolean> generator(@RequestBody Long id) {
        return Result.success(this.service.generator(id));
    }
}
