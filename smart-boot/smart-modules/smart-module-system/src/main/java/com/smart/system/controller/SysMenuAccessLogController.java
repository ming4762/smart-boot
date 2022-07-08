package com.smart.system.controller;

import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SysMenuAccessLogPO;
import com.smart.system.pojo.dto.log.SysMenuAccessLogSaveDTO;
import com.smart.system.service.SysMenuAccessLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 系统菜单访问记录
 * @author ShiZhongMing
 * 2022/7/8
 * @since 3.0.0
 */
@RestController
@RequestMapping("sys/menuAccessLog")
@Tag(name = "系统菜单访问记录")
public class SysMenuAccessLogController extends BaseController<SysMenuAccessLogService, SysMenuAccessLogPO> {


    @Override
    @PostMapping("list")
    @Operation(summary = "查询访问记录列表")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @PostMapping("getById")
    @Operation(summary = "根据ID查询访问记录")
    public Result<SysMenuAccessLogPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @PostMapping("save")
    @Operation(summary = "保存菜单访问记录")
    public Result<Boolean> save(@RequestBody SysMenuAccessLogSaveDTO parameter) {
        SysMenuAccessLogPO model = new SysMenuAccessLogPO();
        model.setFunctionId(parameter.getFunctionId());
        return Result.success(this.service.saveWithUser(model, AuthUtils.getNonNullCurrentUserId()));
    }
}
