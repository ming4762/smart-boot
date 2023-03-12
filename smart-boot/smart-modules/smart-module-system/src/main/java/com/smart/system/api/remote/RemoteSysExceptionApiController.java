package com.smart.system.api.remote;

import com.smart.module.api.system.SysExceptionApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.SysExceptionSaveDTO;
import com.smart.system.api.local.LocalSysExceptionApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhongming4762
 * 2023/3/12
 */
@RestController
@RequestMapping
public class RemoteSysExceptionApiController implements SysExceptionApi {

    private final LocalSysExceptionApi sysExceptionApi;

    public RemoteSysExceptionApiController(LocalSysExceptionApi sysExceptionApi) {
        this.sysExceptionApi = sysExceptionApi;
    }

    /**
     * 保存异常信息
     *
     * @param parameter 参数
     * @return 异常信息ID
     */
    @Override
    @PostMapping(SystemApiUrlConstants.EXCEPTION_SAVE)
    public Boolean saveException(@RequestBody SysExceptionSaveDTO parameter) {
        return this.sysExceptionApi.saveException(parameter);
    }
}
