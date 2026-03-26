package com.smart.module.system.controller.monitor;

import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.module.system.model.monitor.SmartMonitorSlowSqlPO;
import com.smart.module.system.service.monitor.SmartMonitorSlowSqlService;
import io.swagger.v3.oas.annotations.Operation;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
* smart_monitor_slow_sql - 慢sql记录 Controller
* @author SmartCodeGenerator
* 2025年3月1日 20:58:16
*/
@RestController
@RequestMapping("/sys/monitor/sqlSql")
public class SmartMonitorSlowSqlController extends BaseController<SmartMonitorSlowSqlService, SmartMonitorSlowSqlPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SmartMonitorSlowSqlPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}