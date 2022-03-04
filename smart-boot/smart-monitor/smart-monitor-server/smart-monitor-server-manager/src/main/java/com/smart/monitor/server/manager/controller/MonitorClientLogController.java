package com.smart.monitor.server.manager.controller;

import com.smart.commons.core.message.Result;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.monitor.server.manager.model.MonitorClientLogPO;
import com.smart.monitor.server.manager.service.MonitorClientLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
* monitor_client_log - 客户端日志 Controller
* @author GCCodeGenerator
* 2022-3-2
*/
@RestController
@RequestMapping("monitor/client/log")
public class MonitorClientLogController extends BaseController<MonitorClientLogService, MonitorClientLogPO> {

    @PostMapping("listFilterUser")
    @ApiOperation(value = "查询客户端日志列表（支持分页、实体类属性查询）")
    public Result<Object> listFilterUser(@RequestBody @NonNull PageSortQuery parameter) {
        parameter.getParameter().put(CrudCommonEnum.FILTER_BY_USER.name(), true);
        return super.list(parameter);
    }


    @Override
    @ApiOperation(value = "通过ID查询")
    @PostMapping("getById")
    public Result<MonitorClientLogPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

}
