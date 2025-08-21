package com.smart.module.code.controller;

import com.smart.framework.commons.core.message.Result;
import com.smart.module.code.pojo.dto.DbGenerateMapperBySqlParameter;
import com.smart.module.code.pojo.vo.DbCodeVO;
import com.smart.module.code.service.DbCodeMainService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author shizhongming
 * 2025/8/19 11:23
 * @since 5.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/db/code/report")
public class DbCodeReportController {

    private final DbCodeMainService dbCodeMainService;

    @Operation(summary = "根据SQL生成Mapper")
    @PostMapping("/generateMapperBySql")
    public Result<List<DbCodeVO>> generateMapperBySql(@RequestBody @Valid DbGenerateMapperBySqlParameter parameter) {
        return Result.success(dbCodeMainService.generateMapperBySql(parameter));
    }
}