package com.smart.monitor.server.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.message.Result;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.monitor.server.manager.model.MonitorClientHttpTracePO;
import com.smart.monitor.server.manager.pojo.dto.ClientIdQueryDTO;
import com.smart.monitor.server.manager.service.MonitorClientHttpTraceService;
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


    @PostMapping("listClientId")
    @ApiOperation(value = "查询客户端ID列表")
    public Result<List<String>> listClientId(@RequestBody ClientIdQueryDTO parameter) {
        QueryWrapper<MonitorClientHttpTracePO> queryWrapper = new QueryWrapper<MonitorClientHttpTracePO>().select("DISTINCT client_id");
        if (StringUtils.isNotBlank(parameter.getApplicationName())) {
            queryWrapper.lambda().eq(MonitorClientHttpTracePO :: getApplicationCode, parameter.getApplicationName());
        }
        return Result.success(
                this.service.list(queryWrapper).stream()
                        .map(MonitorClientHttpTracePO::getClientId)
                        .collect(Collectors.toList())
        );
    }
}
