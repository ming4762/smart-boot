package com.smart.monitor.server.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.message.Result;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.monitor.server.manager.model.MonitorClientLogPO;
import com.smart.monitor.server.manager.pojo.dto.ClientIdQueryDTO;
import com.smart.monitor.server.manager.service.MonitorClientLogService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("listClientId")
    @ApiOperation(value = "查询客户端ID列表")
    public Result<List<String>> listClientId(@RequestBody ClientIdQueryDTO parameter) {
        QueryWrapper<MonitorClientLogPO> queryWrapper = new QueryWrapper<MonitorClientLogPO>().select("DISTINCT client_id");
        if (StringUtils.isNotBlank(parameter.getApplicationName())) {
            queryWrapper.lambda().eq(MonitorClientLogPO :: getApplicationCode, parameter.getApplicationName());
        }
        return Result.success(
                this.service.list(queryWrapper).stream()
                        .map(MonitorClientLogPO::getClientId)
                        .collect(Collectors.toList())
        );
    }


    @Override
    @ApiOperation(value = "通过ID查询")
    @PostMapping("getById")
    public Result<MonitorClientLogPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

}
