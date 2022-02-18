package com.smart.monitor.server.manager.controller;

import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.monitor.server.core.constants.BuiltInEventCodeEnum;
import com.smart.monitor.server.manager.model.MonitorEventPO;
import com.smart.monitor.server.manager.service.MonitorEventService;
import io.swagger.annotations.ApiOperation;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* monitor_event - 事件信息 Controller
* @author GCCodeGenerator
* 2022-2-18 12:04:12
*/
@RestController
@RequestMapping("monitor/manager/event")
public class MonitorEventController extends BaseController<MonitorEventService, MonitorEventPO> {

    @Override
    @PostMapping("list")
    @ApiOperation(value = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @ApiOperation(value = "通过ID查询")
    @PostMapping("getById")
    public Result<MonitorEventPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @ApiOperation("查询内置的事件编码")
    @PostMapping("listBuiltInEventCode")
    public Result<List<String>> listBuiltInEventCode() {
        return Result.success(
                Arrays.stream(BuiltInEventCodeEnum.values())
                        .map(BuiltInEventCodeEnum::getCode)
                        .collect(Collectors.toList())
        );
    }
}
