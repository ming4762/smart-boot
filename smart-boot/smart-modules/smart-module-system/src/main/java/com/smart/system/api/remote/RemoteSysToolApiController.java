package com.smart.system.api.remote;

import com.smart.module.api.system.SysToolApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.SerialCodeCreateDTO;
import com.smart.module.api.system.dto.SmartChangeLogListDTO;
import com.smart.module.api.system.parameter.RemoteChangeLogListParameter;
import com.smart.module.api.system.parameter.RemoteChangeLogSaveParameter;
import com.smart.module.api.system.parameter.SerialCodeCreateParameter;
import com.smart.system.api.local.LocalSysToolApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/6/2
 */
@RestController
@RequestMapping
public class RemoteSysToolApiController implements SysToolApi {

    private final LocalSysToolApi localSysToolApi;

    public RemoteSysToolApiController(LocalSysToolApi localSysToolApi) {
        this.localSysToolApi = localSysToolApi;
    }

    /**
     * 获取业务编码
     *
     * @param parameter 参数
     * @return 业务编码
     */
    @Override
    @PostMapping(SystemApiUrlConstants.TOOL_CREATE_SERIAL)
    public SerialCodeCreateDTO createSerial(@RequestBody SerialCodeCreateParameter parameter) {
        return this.localSysToolApi.createSerial(parameter);
    }

    /**
     * 批量获取业务编码
     *
     * @param parameterList 参数列表
     * @return 业务编码列表
     */
    @Override
    @PostMapping(SystemApiUrlConstants.TOOL_CREATE_SERIAL_BATCH)
    public List<SerialCodeCreateDTO> createSerial(@RequestBody List<SerialCodeCreateParameter> parameterList) {
        return this.localSysToolApi.createSerial(parameterList);
    }

    /**
     * 保存修改记录
     *
     * @param parameter 参数
     * @return 是否保存成功
     */
    @Override
    @PostMapping(SystemApiUrlConstants.TOOL_CHANGE_LOG_SAVE)
    public boolean saveChangeLog(@RequestBody RemoteChangeLogSaveParameter parameter) {
        return this.localSysToolApi.saveChangeLog(parameter);
    }

    /**
     * 查询修改记录
     *
     * @param parameter 参数
     * @return 修改记录列表
     */
    @Override
    @PostMapping(SystemApiUrlConstants.TOOL_CHANGE_LIST)
    public List<SmartChangeLogListDTO> listChangeLog(@RequestBody RemoteChangeLogListParameter parameter) {
        return this.localSysToolApi.listChangeLog(parameter);
    }
}
