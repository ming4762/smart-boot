package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysLogApi;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.module.api.system.dto.SysLogSaveDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2025/6/13 19:19
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteSysLogApiFallback implements RemoteSysLogApi {
    @Override
    public Boolean saveLog(SysLogSaveDTO parameter) {
        log.error("saveLog error,log:{}", JsonUtils.toJsonString(parameter));
        return false;
    }
}
