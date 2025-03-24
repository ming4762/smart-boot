package com.smart.cloud.api.system.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.system.SysExceptionApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.SysExceptionSaveDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author zhongming4762
 * 2023/3/12
 */
@FeignClient(value = CloudServiceNameConstants.SYSTEM_SERVICE, contextId = "sysExceptionApi")
public interface RemoteSysExceptionApi extends SysExceptionApi {

    /**
     * 保存异常信息
     *
     * @param parameter 参数
     * @return 异常信息ID
     */
    @Override
    @PostMapping(SystemApiUrlConstants.EXCEPTION_SAVE)
    Boolean saveException(SysExceptionSaveDTO parameter);
}
