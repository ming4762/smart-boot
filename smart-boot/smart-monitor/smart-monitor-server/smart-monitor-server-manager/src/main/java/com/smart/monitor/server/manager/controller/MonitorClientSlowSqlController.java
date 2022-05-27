package com.smart.monitor.server.manager.controller;

import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.monitor.server.manager.model.MonitorClientSlowSqlPO;
import com.smart.monitor.server.manager.service.MonitorClientSlowSqlService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
* monitor_client_slow_sql - 客户端慢SQL Controller
* @author GCCodeGenerator
* 2022-2-24 10:57:04
*/
@RestController
@RequestMapping("monitor/slowSql")
public class MonitorClientSlowSqlController extends BaseController<MonitorClientSlowSqlService, MonitorClientSlowSqlPO> {


    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }


    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<MonitorClientSlowSqlPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}
