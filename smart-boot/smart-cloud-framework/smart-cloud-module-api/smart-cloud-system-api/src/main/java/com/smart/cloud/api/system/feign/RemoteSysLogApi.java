package com.smart.cloud.api.system.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.SysLogSaveDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 系统模块-系统日志feign调用接口
 * @author zhongming4762
 * 2023/3/11
 */
@FeignClient(value = CloudServiceNameConstants.SYSTEM_SERVICE, contextId = "remoteSysLogApi")
public interface RemoteSysLogApi extends SysLogApi {

    /**
     * 保存日志
     *
     * @param parameter 参数
     * @return 是否保存成功
     */
    @Override
    @PostMapping(SystemApiUrlConstants.LOG_SAVE)
    Boolean saveLog(SysLogSaveDTO parameter);
}
