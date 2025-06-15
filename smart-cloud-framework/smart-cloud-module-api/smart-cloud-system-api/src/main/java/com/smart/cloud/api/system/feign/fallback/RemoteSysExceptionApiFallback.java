package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysExceptionApi;
import com.smart.module.api.system.dto.SysExceptionSaveDTO;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2025/6/13 19:18
 * @since 5.0.0
 */
@Component
public class RemoteSysExceptionApiFallback implements RemoteSysExceptionApi {
    @Override
    public Boolean saveException(SysExceptionSaveDTO parameter) {
        return false;
    }
}
