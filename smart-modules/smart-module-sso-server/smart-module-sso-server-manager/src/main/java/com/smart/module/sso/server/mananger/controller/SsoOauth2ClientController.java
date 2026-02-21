package com.smart.module.sso.server.mananger.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.PageData;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.parameter.SetUseYnParameter;
import com.smart.framework.crud.query.IdPageSortQuery;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.utils.CrudPageHelper;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.sso.server.common.manager.model.SsoOauth2ClientPO;
import com.smart.module.sso.server.mananger.pojo.dto.SsoOauth2ClientSaveUpdateDTO;
import com.smart.module.sso.server.mananger.pojo.parameter.SsoClientBindUserParameter;
import com.smart.module.sso.server.mananger.pojo.parameter.SsoClientUnBindUserParameter;
import com.smart.module.sso.server.mananger.pojo.parameter.SsoClientUserUseYnParameter;
import com.smart.module.sso.server.mananger.pojo.parameter.SsoListClientUserParameter;
import com.smart.module.sso.server.mananger.pojo.vo.SsoClientUserVO;
import com.smart.module.sso.server.mananger.service.SsoOauth2ClientService;
import com.smart.module.system.model.SysUserPO;
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
public class SsoOauth2ClientController extends BaseController<SsoOauth2ClientService, SsoOauth2ClientPO> {

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
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SsoOauth2ClientSaveUpdateDTO> parameterList) {
        List<SsoOauth2ClientPO> modelList = parameterList.stream().map(item -> {
            SsoOauth2ClientPO model = new SsoOauth2ClientPO();
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
    public Result<SsoOauth2ClientPO> getById(@RequestBody Serializable id) {
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

    @Operation(summary = "查询客户端对应的用户")
    @PostMapping("listClientUser")
    public Result<PageData<SsoClientUserVO>> listClientUser(@RequestBody @NonNull SsoListClientUserParameter parameter) {
        Page<SysUserPO> page = CrudPageHelper.createPage(SysUserPO.class, parameter);
        QueryWrapper<SysUserPO> queryWrapper = CrudUtils.createQueryWrapperFromParameters(parameter, SysUserPO.class);
        List<SsoClientUserVO> dataList = CrudPageHelper.withPage(page, () -> this.service.listClientUser(parameter, queryWrapper));
        return Result.success(PageData.of(dataList, page.getTotal()));
    }

    @Operation(summary = "查询未绑定用户列表")
    @PostMapping("listUnBindUser")
    public Result<PageData<SysUserDTO>> listUnBindUser(@RequestBody @Valid IdPageSortQuery parameter) {
        QueryWrapper<SysUserPO> queryWrapper = CrudUtils.createQueryWrapperFromParameters(parameter, SysUserPO.class);
        Page<SysUserPO> page = this.doPage(parameter);
        Long clientId = parameter.getId();
        List<SysUserDTO> userList = CrudPageHelper.withPage(page, () -> this.service.listUnBindUser(clientId, queryWrapper));
        return Result.success(PageData.of(userList, page.getTotal()));
    }

    @Operation(summary = "绑定用户到客户端")
    @PostMapping("bindUser")
    @Log(value = "绑定用户到oauth2客户端", type = LogOperationTypeEnum.UPDATE)
//    @PreAuthorize("hasPermission('sso:clientUser', 'update')")
    public Result<Boolean> bindUser(@RequestBody @Valid SsoClientBindUserParameter parameter) {
        return Result.success(this.service.bindUser(parameter));
    }

    @Operation(summary = "解绑用户到客户端")
    @PostMapping("unBindUser")
    @Log(value = "解绑用户与oauth2客户端", type = LogOperationTypeEnum.UPDATE)
//    @PreAuthorize("hasPermission('sso:clientUser', 'update')")
    public Result<Boolean> unBindUser(@RequestBody @Valid SsoClientUnBindUserParameter parameter) {
        return Result.success(this.service.unBindUser(parameter));
    }

    @Operation(summary = "设置客户端用户启用状态")
    @PostMapping("setBindUserUseYn")
    @Log(value = "设置客户端用户启用状态", type = LogOperationTypeEnum.UPDATE)
//    @PreAuthorize("hasPermission('sso:clientUser', 'update')")
    public Result<Boolean> setBindUserUseYn(@RequestBody @Valid SsoClientUserUseYnParameter parameter) {
        return Result.success(this.service.setBindUserUseYn(parameter));
    }
}