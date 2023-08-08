package com.smart.system.controller.change;

import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.module.api.system.SysToolApi;
import com.smart.module.api.system.dto.SmartChangeLogListDTO;
import com.smart.module.api.system.parameter.RemoteChangeLogListParameter;
import com.smart.system.model.SmartChangeLogPO;
import com.smart.system.service.change.SmartChangeLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/7/31 18:02
 */
@RestController
@RequestMapping("smart/changeLog")
@Tag(name = "修改记录接口")
public class SmartChangeLogController extends BaseController<SmartChangeLogService, SmartChangeLogPO> {

    private final SysToolApi sysToolApi;

    public SmartChangeLogController(SysToolApi sysToolApi) {
        this.sysToolApi = sysToolApi;
    }

    @PostMapping("list")
    @Operation(summary = "查询修改记录")
    public Result<List<SmartChangeLogListDTO>> list(@RequestBody RemoteChangeLogListParameter parameter) {
        return Result.success(this.sysToolApi.listChangeLog(parameter));
    }
}
