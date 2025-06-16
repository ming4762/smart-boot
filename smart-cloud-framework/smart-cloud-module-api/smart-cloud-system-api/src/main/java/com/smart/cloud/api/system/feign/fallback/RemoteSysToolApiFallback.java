package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysToolApi;
import com.smart.module.api.system.dto.SerialCodeCreateDTO;
import com.smart.module.api.system.dto.SmartChangeLogListDTO;
import com.smart.module.api.system.parameter.RemoteChangeLogListParameter;
import com.smart.module.api.system.parameter.RemoteChangeLogSaveParameter;
import com.smart.module.api.system.parameter.SerialCodeCreateParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shizhongming
 * 2025/6/15 20:51
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteSysToolApiFallback implements FallbackFactory<RemoteSysToolApi> {

    @Override
    public RemoteSysToolApi create(Throwable cause) {
        return new RemoteSysToolApi() {
            private void errorLog() {
                log.error("RemoteSysToolApiFallback", cause);
            }
            @Override
            public SerialCodeCreateDTO createSerial(SerialCodeCreateParameter parameter) {
                this.errorLog();
                return null;
            }

            @Override
            public List<SerialCodeCreateDTO> createSerial(List<SerialCodeCreateParameter> parameterList) {
                this.errorLog();
                return List.of();
            }

            @Override
            public boolean saveChangeLog(RemoteChangeLogSaveParameter parameter) {
                this.errorLog();
                return false;
            }

            @Override
            public List<SmartChangeLogListDTO> listChangeLog(RemoteChangeLogListParameter parameter) {
                this.errorLog();
                return List.of();
            }
        };
    }
}
