package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysToolApi;
import com.smart.module.api.system.dto.SerialCodeCreateDTO;
import com.smart.module.api.system.dto.SmartChangeLogListDTO;
import com.smart.module.api.system.parameter.RemoteChangeLogListParameter;
import com.smart.module.api.system.parameter.RemoteChangeLogSaveParameter;
import com.smart.module.api.system.parameter.SerialCodeCreateParameter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shizhongming
 * 2025/6/15 20:51
 * @since 5.0.0
 */
@Component
public class RemoteSysToolApiFallback implements RemoteSysToolApi {
    @Override
    public SerialCodeCreateDTO createSerial(SerialCodeCreateParameter parameter) {
        return null;
    }

    @Override
    public List<SerialCodeCreateDTO> createSerial(List<SerialCodeCreateParameter> parameterList) {
        return List.of();
    }

    @Override
    public boolean saveChangeLog(RemoteChangeLogSaveParameter parameter) {
        return false;
    }

    @Override
    public List<SmartChangeLogListDTO> listChangeLog(RemoteChangeLogListParameter parameter) {
        return List.of();
    }
}
