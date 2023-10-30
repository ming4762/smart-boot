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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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

    @Operation(summary = "批量添加修改")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改", type = LogOperationTypeEnum.UPDATE)
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SysAuthAccessSecretSaveUpdateDTO> parameterList) {
      	List<SysAuthAccessSecretPO> modelList = parameterList.stream().map(item -> {
            SysAuthAccessSecretPO model = new SysAuthAccessSecretPO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).collect(Collectors.toList());
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除", type = LogOperationTypeEnum.DELETE)
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