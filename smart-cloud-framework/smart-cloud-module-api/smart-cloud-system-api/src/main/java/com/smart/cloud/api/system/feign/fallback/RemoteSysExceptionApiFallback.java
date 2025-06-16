package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysExceptionApi;
import com.smart.module.api.system.dto.SysExceptionSaveDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2025/6/13 19:18
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteSysExceptionApiFallback implements FallbackFactory<RemoteSysExceptionApi> {

    @Override
    public RemoteSysExceptionApi create(Throwable cause) {
        return new RemoteSysExceptionApi() {

            private void errorLog() {
                log.error("RemoteSysExceptionApiFallback", cause);
            }

            @Override
            public Boolean saveException(SysExceptionSaveDTO parameter) {
                this.errorLog();
                return false;
            }
        };
    }
}
