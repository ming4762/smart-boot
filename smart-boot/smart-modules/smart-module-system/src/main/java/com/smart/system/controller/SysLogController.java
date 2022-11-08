package com.smart.system.controller;

import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SysLogPO;
import com.smart.system.service.SysLogService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0.7
 */
@RestController
@RequestMapping("sys/log")
public class SysLogController extends BaseController<SysLogService, SysLogPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询日志信息", method = "POST")
    @PreAuthorize("hasPermission('sys:log', 'query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @PostMapping("getById")
    @Operation(summary = "通过ID查询日志信息", method = "POST")
    @PreAuthorize("hasPermission('sys:log', 'query')")
    public Result<SysLogPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}
