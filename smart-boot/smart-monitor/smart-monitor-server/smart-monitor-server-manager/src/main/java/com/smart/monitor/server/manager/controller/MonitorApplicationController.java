package com.smart.monitor.server.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.monitor.server.manager.model.MonitorApplicationPO;
import com.smart.monitor.server.manager.model.MonitorUserGroupApplicationPO;
import com.smart.monitor.server.manager.pojo.dto.MonitorApplicationSaveUpdateDTO;
import com.smart.monitor.server.manager.pojo.dto.MonitorApplicationSetUserGroupDTO;
import com.smart.monitor.server.manager.service.MonitorApplicationService;
import com.smart.monitor.server.manager.service.MonitorUserGroupApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
* monitor_application - 应用管理表 Controller
* @author GCCodeGenerator
* 2022-2-8 16:24:43
*/
@RestController
@RequestMapping("monitor/manager/application")
public class MonitorApplicationController extends BaseController<MonitorApplicationService, MonitorApplicationPO> {

    private final MonitorUserGroupApplicationService monitorUserGroupApplicationService;

    public MonitorApplicationController(MonitorUserGroupApplicationService monitorUserGroupApplicationService) {
        this.monitorUserGroupApplicationService = monitorUserGroupApplicationService;
    }


    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "添加修改应用管理表")
    @PostMapping("saveUpdate")
    @PreAuthorize("hasPermission('monitor:application', 'save') or hasPermission('monitor:application', 'update')")
    @Log(value = "添加修改应用管理表", type = LogOperationTypeEnum.UPDATE)
    public Result<Boolean> saveUpdate(@RequestBody @Valid MonitorApplicationSaveUpdateDTO parameter) {
      	MonitorApplicationPO model = new MonitorApplicationPO();
      	BeanUtils.copyProperties(parameter, model);
        return super.saveUpdate(model);
    }

    @Override
    @Operation(summary = "通过ID批量删除应用管理表")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除应用管理表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('monitor:application', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<MonitorApplicationPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @PostMapping("listUserGroupById")
    @Operation(summary = "查询应用关联的用户组ID")
    public Result<List<Long>> listUserGroupById(@RequestBody Long id) {
        return Result.success(
                this.monitorUserGroupApplicationService.list(
                        new QueryWrapper<MonitorUserGroupApplicationPO>().lambda()
                                .select(MonitorUserGroupApplicationPO :: getUserGroupId)
                                .eq(MonitorUserGroupApplicationPO :: getApplicationId, id)
                ).stream().map(MonitorUserGroupApplicationPO :: getUserGroupId).collect(Collectors.toList())
        );
    }

    @PreAuthorize("hasPermission('monitor:application', 'setUserGroup')")
    @PostMapping("setUserGroup")
    @Operation(summary = "设置应用关联的用户组")
    @Log(value = "设置应用关联的用户组", type = LogOperationTypeEnum.UPDATE, saveResult = true)
    public Result<Boolean> setUserGroup(@RequestBody MonitorApplicationSetUserGroupDTO parameter) {
        return Result.success(this.monitorUserGroupApplicationService.setUserGroup(parameter));
    }

    @PostMapping("listUserApplicationName")
    @Operation(summary = "获取用户关联的应用名称列表")
    public Result<List<String>> listUserApplicationName() {
        return Result.success(this.service.listApplicationNameByUser(AuthUtils.getNonNullCurrentUserId()));
    }
}
