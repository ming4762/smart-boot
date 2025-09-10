package com.smart.module.system.controller.auth;

import com.smart.framework.commons.core.message.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shizhongming
 * 2025/9/9 19:08
 * @since 5.0.0
 */
@RestController
@RequestMapping("access/api/test")
public class SysAuthAccessSecretTestController {

    @PostMapping("test")
    public Result<Map<String, Object>> test(@RequestBody Map<String, Object> bodyParameter, @RequestParam Map<String, Object> queryParameter) {
        HashMap<String, Object> result = new HashMap<>(bodyParameter);
        result.putAll(queryParameter);
        return Result.success(result);
    }
}
