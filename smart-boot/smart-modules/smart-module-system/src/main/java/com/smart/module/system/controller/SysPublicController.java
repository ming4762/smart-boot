package com.smart.module.system.controller;

import com.smart.framework.commons.core.message.Result;
import com.smart.module.api.system.SysParameterApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author shizhongming
 * 2024/12/19 17:21
 * @since 5.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("public/system")
public class SysPublicController {

    private static final List<String> SYSTEM_CODE_LIST = List.of(
            "sys.auth.account.passwordValidate",
            "sys.auth.account.passwordValidateErrorMessage"
    );

    private final SysParameterApi sysParameterApi;

    /**
     * 获取系统参数
     * @return 系统参数
     */
    @PostMapping("getSystemProperties")
    public Result<Map<String, String>> getSystemProperties() {
        return Result.success(this.sysParameterApi.getParameter(SYSTEM_CODE_LIST));
    }
}
