package com.smart.system.controller.auth;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.auth.SysAuthAccessSecretPO;
import com.smart.system.pojo.dto.auth.SysAuthAccessSecretSaveUpdateDTO;
import com.smart.system.service.auth.SysAuthAccessSecretService;
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
* sys_auth_access_secret -  Controller
* @author SmartCodeGenerator
* 2023年10月26日 下午7:25:26
*/
@RestController
@RequestMapping("/sys/auth/accessSecret")
public class SysAuthAccessSecretController extends BaseController<SysAuthAccessSecretService, SysAuthAccessSecretPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    /**
     * 添加更新
     *
     * @param parameter 参数
     */
    @Operation(summary = "添加修改Access Secret")
    @PostMapping("saveUpdate")
    @Log(value = "添加修改Access Secret", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:auth:accessSecret', 'save') or hasPermission('sys:auth:accessSecret', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody @Valid SysAuthAccessSecretSaveUpdateDTO parameter) {
        SysAuthAccessSecretPO model = new SysAuthAccessSecretPO();
        BeanUtils.copyProperties(parameter, model);
        return super.saveUpdate(model);
    }

    @Override
    @Operation(summary = "通过ID批量删除")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:auth:accessSecret', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SysAuthAccessSecretPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}