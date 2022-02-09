package com.smart.monitor.server.manager.controller;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.monitor.server.manager.model.MonitorApplicationPO;
import com.smart.monitor.server.manager.service.MonitorApplicationService;
import com.smart.monitor.server.manager.pojo.dto.MonitorApplicationSaveUpdateDTO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
* monitor_application - 应用管理表 Controller
* @author GCCodeGenerator
* 2022-2-8 16:24:43
*/
@RestController
@RequestMapping("monitor/manager/application/")
public class MonitorApplicationController extends BaseController<MonitorApplicationService, MonitorApplicationPO> {

    @Override
    @PostMapping("list")
    @ApiOperation(value = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @ApiOperation(value = "添加修改应用管理表")
    @PostMapping("saveUpdate")
    @Log(value = "添加修改应用管理表", type = LogOperationTypeEnum.UPDATE)
    public Result<Boolean> saveUpdate(@RequestBody @Valid MonitorApplicationSaveUpdateDTO parameter) {
      	MonitorApplicationPO model = new MonitorApplicationPO();
      	BeanUtils.copyProperties(parameter, model);
        return super.saveUpdate(model);
    }

    @Override
    @ApiOperation(value = "通过ID批量删除应用管理表")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除应用管理表", type = LogOperationTypeEnum.DELETE)
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @ApiOperation(value = "通过ID查询")
    @PostMapping("getById")
    public Result<MonitorApplicationPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}
