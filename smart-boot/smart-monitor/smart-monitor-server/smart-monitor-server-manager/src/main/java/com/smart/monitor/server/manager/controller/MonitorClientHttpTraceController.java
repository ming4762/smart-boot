package com.smart.monitor.server.manager.controller;

import com.smart.commons.core.message.Result;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.monitor.server.manager.model.MonitorClientHttpTracePO;
import com.smart.monitor.server.manager.service.MonitorClientHttpTraceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
* monitor_client_http_trace - 客户端HttpTrace Controller
* @author GCCodeGenerator
* 2022-3-25 13:19:44
*/
@RestController
@RequestMapping("monitor/client/httpTrace")
public class MonitorClientHttpTraceController extends BaseController<MonitorClientHttpTraceService, MonitorClientHttpTracePO> {

    @Override
    @PostMapping("list")
    @ApiOperation(value = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }


    /**
     * 通过当前登录用户查询
     * @param parameter 参数
     * @return http trace 信息
     */
    @PostMapping("listByCurrentUserNoDetail")
    public Result<Object> listByCurrentUserNoDetail(@RequestBody PageSortQuery parameter) {
        parameter.getParameter().put(CrudCommonEnum.FILTER_BY_USER.name(), true);
        parameter.getParameter().put(CrudCommonEnum.ASSIGN_FIELD.name(), true);
        return super.list(parameter);
    }

    @Override
    @ApiOperation(value = "通过ID查询")
    @PostMapping("getById")
    public Result<MonitorClientHttpTracePO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}
