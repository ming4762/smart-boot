package com.smart.system.controller.smart_form_api;

import com.smart.commons.core.message.Result;
import com.smart.system.controller.smart_form_api.dto.SmartFormTableSelectApiDTO;
import com.smart.system.controller.smart_form_api.vo.SmartFormTableSelectApiVO;
import com.smart.system.service.SmartFormApiService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * smart-form组件API接口
 * @author zhongming4762
 * 2023/2/18 17:40
 */
@RestController
@RequestMapping("api/component/smart-form")
public class SmartFormApiController {

    private final SmartFormApiService smartFormApiService;

    public SmartFormApiController(SmartFormApiService smartFormApiService) {
        this.smartFormApiService = smartFormApiService;
    }

    @Operation(summary = "查询table-select数据")
    @PostMapping("listTableSelect")
    public Result<List<SmartFormTableSelectApiVO>> listTableSelect(@RequestBody SmartFormTableSelectApiDTO parameter) {
        return Result.success(this.smartFormApiService.listTableSelect(parameter));
    }
}