package com.smart.sms.manager.controller;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.IdParameter;
import com.smart.crud.query.PageSortQuery;
import com.smart.sms.manager.model.SmartSmsChannelManagerPO;
import com.smart.sms.manager.pojo.parameter.SmartSmsChannelManagerSaveUpdateParameter;
import com.smart.sms.manager.service.SmartSmsChannelManagerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
* smart_sms_channel_manager - 短信通道表 Controller
* @author SmartCodeGenerator
* 2023年5月26日 上午9:34:41
*/
@RestController
@RequestMapping("/smart/sms/channel")
public class SmartSmsChannelManagerController extends BaseController<SmartSmsChannelManagerService, SmartSmsChannelManagerPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('smart:sms', 'query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改短信通道表")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改短信通道表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('smart:sms', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SmartSmsChannelManagerSaveUpdateParameter> parameterList) {
        // 验证 isDefault 是否唯一
//        long isDefaultCount = parameterList.stream()
//                .filter(SmartSmsChannelManagerSaveUpdateParameter::getIsDefault)
//                .count();
//        if (isDefaultCount > 1) {
//            throw new I18nException(SmartSmsI18nMessageEnum.ERROR_SAVE_MULTI_DEFAULT);
//        }
        List<SmartSmsChannelManagerPO> modelList = parameterList.stream().map(item -> {
            SmartSmsChannelManagerPO model = new SmartSmsChannelManagerPO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).collect(Collectors.toList());
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除短信通道表")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除短信通道表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('smart:sms', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    @PreAuthorize("hasPermission('smart:sms', 'query')")
    public Result<SmartSmsChannelManagerPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @Operation(summary = "设置默认通道")
    @PostMapping("setDefault")
    @PreAuthorize("hasPermission('smart:sms', 'update')")
    public Result<Boolean> setDefault(@RequestBody IdParameter idParameter) {
        return Result.success(this.service.setDefault((Long)idParameter.getId()));
    }
}