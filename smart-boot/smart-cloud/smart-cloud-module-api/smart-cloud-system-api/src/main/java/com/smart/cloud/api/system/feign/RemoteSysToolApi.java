package com.smart.cloud.api.system.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.system.SysToolApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.SerialCodeCreateDTO;
import com.smart.module.api.system.dto.SmartChangeLogListDTO;
import com.smart.module.api.system.parameter.RemoteChangeLogListParameter;
import com.smart.module.api.system.parameter.RemoteChangeLogSaveParameter;
import com.smart.module.api.system.parameter.SerialCodeCreateParameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/6/2 17:05
 */
@FeignClient(value = CloudServiceNameConstants.SYSTEM_SERVICE, contextId = "remoteSysToolApi")
public interface RemoteSysToolApi extends SysToolApi {

    /**
     * 获取业务编码
     *
     * @param parameter 参数
     * @return 业务编码
     */
    @Override
    @PostMapping(SystemApiUrlConstants.TOOL_CREATE_SERIAL)
    SerialCodeCreateDTO createSerial(SerialCodeCreateParameter parameter);

    /**
     * 批量获取业务编码
     *
     * @param parameterList 参数列表
     * @return 业务编码列表
     */
    @Override
    @PostMapping(SystemApiUrlConstants.TOOL_CREATE_SERIAL_BATCH)
    List<SerialCodeCreateDTO> createSerial(List<SerialCodeCreateParameter> parameterList);

    /**
     * 保存修改记录
     *
     * @param parameter 参数
     * @return 是否保存成功
     */
    @Override
    @PostMapping(SystemApiUrlConstants.TOOL_CHANGE_LOG_SAVE)
    boolean saveChangeLog(RemoteChangeLogSaveParameter parameter);

    /**
     * 查询修改记录
     *
     * @param parameter 参数
     * @return 修改记录列表
     */
    @Override
    @PostMapping(SystemApiUrlConstants.TOOL_CHANGE_LIST)
    List<SmartChangeLogListDTO> listChangeLog(RemoteChangeLogListParameter parameter);
}
