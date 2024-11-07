package com.smart.module.message.controller;

import com.smart.framework.commons.core.dto.common.LabelValueData;
import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.commons.core.utils.EnumUtils;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.parameter.SetUseYnParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.message.core.constants.SmartMessageChannelType1Enum;
import com.smart.framework.message.core.constants.SmartMessageChannelType2Enum;
import com.smart.module.message.model.SmartMessageChannelPO;
import com.smart.module.message.pojo.dbo.SmartMessageChannelSaveUpdateDTO;
import com.smart.module.message.service.SmartMessageChannelService;
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
* smart_message_channel - 消息通道信息 Controller
* @author SmartCodeGenerator
* 2024年5月17日 下午5:13:58
*/
@RestController
@RequestMapping("/smart/message/channel/")
public class SmartMessageChannelController extends BaseController<SmartMessageChannelService, SmartMessageChannelPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改消息通道信息")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改消息通道信息", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('smart:message:channel', 'save') or hasPermission('smart:message:channel', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SmartMessageChannelSaveUpdateDTO> parameterList) {
        List<SmartMessageChannelPO> modelList = parameterList.stream().map(item -> {
            SmartMessageChannelPO model = new SmartMessageChannelPO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).toList();
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除消息通道信息")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除消息通道信息", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('smart:message:channel', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SmartMessageChannelPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 设置启用停用
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    @Operation(summary = "启用停用消息通道信息")
    @PostMapping("setUseYn")
    @Log(value = "启用停用消息通道信息", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('smart:message:channel', 'setUseYn')")
    public Result<Boolean> setUseYn(@RequestBody @Valid SetUseYnParameter parameter) {
        return super.setUseYn(parameter);
    }

    /**
     * 查询枚举类 listEnumLabelValue
     *
     * @return label value
     */
    @Operation(summary = "查询一级通道列表")
    @PostMapping("listSmartMessageType1Enum")
    public Result<List<LabelValueData<SmartMessageChannelType1Enum>>> listSmartMessageType1Enum() {
        List<LabelValueData<SmartMessageChannelType1Enum>> dataList = EnumUtils.convertLabelValue(SmartMessageChannelType1Enum.class).stream()
                .peek(item -> item.setData(item.getEnumData().getBuiltIn()))
                .toList();
        return Result.success(dataList);
    }

    /**
     * 查询枚举类 listEnumLabelValue
     *
     * @return label value
     */
    @Operation(summary = "查询二级通道列表")
    @PostMapping("listSmartMessageType2Enum")
    public Result<List<LabelValueData<SmartMessageChannelType2Enum>>> listSmartMessageType2Enum() {
        List<LabelValueData<SmartMessageChannelType2Enum>> dataList = EnumUtils.convertLabelValue(SmartMessageChannelType2Enum.class).stream()
                .peek(item -> item.setData(item.getEnumData().getSmartMessageType1().getValue())).toList();
        return Result.success(dataList);
    }
}