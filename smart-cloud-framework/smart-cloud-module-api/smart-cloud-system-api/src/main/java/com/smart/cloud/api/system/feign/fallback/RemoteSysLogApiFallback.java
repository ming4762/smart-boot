package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysLogApi;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.module.api.system.dto.SysLogSaveDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2025/6/13 19:19
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteSysLogApiFallback implements FallbackFactory<RemoteSysLogApi> {

    @Override
    public RemoteSysLogApi create(Throwable cause) {
        return new RemoteSysLogApi() {

            @Override
            public Boolean saveLog(SysLogSaveDTO parameter) {
                log.error("RemoteSysLogApiFallback, parameter:{}", JsonUtils.toJsonString(parameter), cause);
                return false;
            }
        };
    }
}
