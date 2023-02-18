package com.smart.file.manager.controller;

import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.file.manager.model.SmartFilePO;
import com.smart.file.manager.service.SmartFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author shizhongming
 * 2020/1/27 7:51 下午
 */
@Slf4j
@Tag(name = "文件管理", description = "文件管理")
@RequestMapping("smart/file")
@RestController
public class SmartFileController extends BaseController<SmartFileService, SmartFilePO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询文件列表", method = "POST")
    @PreAuthorize("hasPermission('smart:file', 'query')")
    @ResponseBody
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }
}
