package com.smart.message.manager.controller;

import com.smart.commons.core.message.Result;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.message.manager.model.SmartSmsLogPO;
import com.smart.message.manager.service.SmartSmsLogService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
* smart_sms_log - 短息发送日志 Controller
* @author SmartCodeGenerator
* 2023年5月30日
*/
@RestController
@RequestMapping("/smart/sms/log")
public class SmartSmsLogController extends BaseController<SmartSmsLogService, SmartSmsLogPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询短息发送日志列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('smart:smsLog', 'query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @PostMapping("listAll")
    @Operation(summary = "查询短息发送日志列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('smart:smsLog', 'query')")
    public Result<Object> listAll(@RequestBody @NonNull PageSortQuery parameter) {
        parameter.getParameter().put(CrudCommonEnum.WITH_ALL.name(), true);
        return super.list(parameter);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    @PreAuthorize("hasPermission('smart:smsLog', 'query')")
    public Result<SmartSmsLogPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}