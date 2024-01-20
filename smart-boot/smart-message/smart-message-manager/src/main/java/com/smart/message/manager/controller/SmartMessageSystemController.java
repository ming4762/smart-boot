package com.smart.message.manager.controller;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.message.manager.model.SmartMessageSystemPO;
import com.smart.message.manager.pojo.paramteter.SmartMessageSystemSaveUpdateParameter;
import com.smart.message.manager.pojo.vo.SmartMessageSystemDetailVO;
import com.smart.message.manager.service.SmartMessageSystemService;
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
* smart_message_system - 系统消息表 Controller
* @author SmartCodeGenerator
* 2023年7月6日 下午4:53:46
*/
@RestController
@RequestMapping("/smart/message/systemMessage")
public class SmartMessageSystemController extends BaseController<SmartMessageSystemService, SmartMessageSystemPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改系统消息表")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改系统消息表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('smart:message:systemMessage', 'save') or hasPermission('smart:message:systemMessage', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SmartMessageSystemSaveUpdateParameter> parameterList) {
      	List<SmartMessageSystemPO> modelList = parameterList.stream().map(item -> {
            SmartMessageSystemPO model = new SmartMessageSystemPO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).toList();
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除系统消息表")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除系统消息表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('smart:message:systemMessage', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SmartMessageSystemPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @Operation(summary = "通过ID查询详情")
    @PostMapping("getDetailById")
    public Result<SmartMessageSystemDetailVO> getDetailById(@RequestBody Long id) {
        return Result.success(this.service.getDetailById(id));
    }

    @Operation(summary = "批量发布消息")
    @PostMapping("publish")
    @PreAuthorize("hasPermission('smart:message:systemMessage', 'publish')")
    public Result<Boolean> publish(@RequestBody List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return Result.success(this.service.publish(idList));
    }

    @Operation(summary = "批量撤销消息")
    @PostMapping("revoke")
    @PreAuthorize("hasPermission('smart:message:systemMessage', 'revoke')")
    public Result<Boolean> revoke(@RequestBody List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return Result.success(this.service.revoke(idList));
    }
}