package com.smart.system.controller;

import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SysExceptionPO;
import com.smart.system.pojo.dto.exception.ExceptionFeedbackDTO;
import com.smart.system.service.SysExceptionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
* sys_exception - 系统异常信息 Controller
* @author GCCodeGenerator
* 2022年6月10日
*/
@RestController
@RequestMapping("sys/exception")
@Tag(name = "系统异常管理")
public class SysExceptionController extends BaseController<SysExceptionService, SysExceptionPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询异常列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('sys:exception', 'query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }



    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    @PreAuthorize("hasPermission('sys:exception', 'query')")
    public Result<SysExceptionPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @PostMapping("feedback")
    @Operation(summary = "用户反馈异常信息接口")
    public Result<Boolean> feedback(@RequestBody @Valid ExceptionFeedbackDTO parameter) {
        return Result.success(this.service.feedback(parameter));
    }
}
