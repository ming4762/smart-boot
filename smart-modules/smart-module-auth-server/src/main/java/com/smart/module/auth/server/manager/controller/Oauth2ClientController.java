package com.smart.module.auth.server.manager.controller;

import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.parameter.SetUseYnParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.module.auth.server.manager.model.Oauth2ClientPO;
import com.smart.module.auth.server.manager.pojo.dto.Oauth2ClientSaveUpdateDTO;
import com.smart.module.auth.server.manager.service.Oauth2ClientService;
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
* oauth2_client - oauth2客户端 Controller
* @author SmartCodeGenerator
* 2025年11月14日 16:54:04
*/
@RestController
@RequestMapping("sso/oauth2/client")
public class Oauth2ClientController extends BaseController<Oauth2ClientService, Oauth2ClientPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询oauth2客户端列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改oauth2客户端")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改oauth2客户端", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sso:oauth2:client', 'save') or hasPermission('sso:oauth2:client', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<Oauth2ClientSaveUpdateDTO> parameterList) {
        List<Oauth2ClientPO> modelList = parameterList.stream().map(item -> {
            Oauth2ClientPO model = new Oauth2ClientPO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).toList();
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除oauth2客户端")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除oauth2客户端", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sso:oauth2:client', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<Oauth2ClientPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 设置启用停用
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    @Operation(summary = "启用停用oauth2客户端")
    @PostMapping("setUseYn")
    @Log(value = "启用停用oauth2客户端", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sso:oauth2:client', 'setUseYn')")
    public Result<Boolean> setUseYn(@RequestBody @Valid SetUseYnParameter parameter) {
        return super.setUseYn(parameter);
    }
}