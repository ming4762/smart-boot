package com.smart.system.api.remote;

import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.SysLogSaveDTO;
import com.smart.system.api.local.LocalSysLogApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志远程调用接口
 * @author zhongming4762
 * 2023/3/11
 */
@RestController
@RequestMapping
public class RemoteSysLogApiController implements SysLogApi {

    private final LocalSysLogApi localSysLogApi;

    public RemoteSysLogApiController(LocalSysLogApi localSysLogApi) {
        this.localSysLogApi = localSysLogApi;
    }

    /**
     * 保存日志
     *
     * @param parameter 参数
     * @return 是否保存成功
     */
    @Override
    @PostMapping(SystemApiUrlConstants.LOG_SAVE)
    public boolean saveLog(SysLogSaveDTO parameter) {
        return localSysLogApi.saveLog(parameter);
    }
}
