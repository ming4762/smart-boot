package com.smart.system.api.local;

import com.smart.module.api.system.SysToolApi;
import com.smart.module.api.system.dto.SerialCodeCreateDTO;
import com.smart.module.api.system.dto.SmartChangeLogListDTO;
import com.smart.module.api.system.parameter.RemoteChangeLogListParameter;
import com.smart.module.api.system.parameter.RemoteChangeLogSaveParameter;
import com.smart.module.api.system.parameter.SerialCodeCreateParameter;
import com.smart.system.service.SmartSerialNoService;
import com.smart.system.service.change.SmartChangeLogService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统工具API
 * @author zhongming4762
 * 2023/6/2
 */
@Component
@Primary
public class LocalSysToolApi implements SysToolApi {

    private final SmartSerialNoService smartSerialNoService;

    private final SmartChangeLogService smartChangeLogService;

    public LocalSysToolApi(SmartSerialNoService smartSerialNoService, SmartChangeLogService smartChangeLogService) {
        this.smartSerialNoService = smartSerialNoService;
        this.smartChangeLogService = smartChangeLogService;
    }

    /**
     * 获取业务编码
     *
     * @param parameter 参数
     * @return 业务编码
     */
    @Override
    public SerialCodeCreateDTO createSerial(SerialCodeCreateParameter parameter) {
        List<SerialCodeCreateDTO> serialList = this.createSerial(List.of(parameter));
        // 比如会有一个，所以无需校验是否为空
        return serialList.get(0);
    }

    /**
     * 批量获取业务编码
     *
     * @param parameterList 参数列表
     * @return 业务编码列表
     */
    @Override
    public List<SerialCodeCreateDTO> createSerial(List<SerialCodeCreateParameter> parameterList) {
        return this.smartSerialNoService.createSerial(parameterList);
    }

    /**
     * 保存修改记录
     *
     * @param parameter 参数
     * @return 是否保存成功
     */
    @Override
    public boolean saveChangeLog(RemoteChangeLogSaveParameter parameter) {
        return this.smartChangeLogService.saveChangeLog(parameter);
    }

    /**
     * 查询修改记录
     *
     * @param parameter 参数
     * @return 修改记录列表
     */
    @Override
    public List<SmartChangeLogListDTO> listChangeLog(RemoteChangeLogListParameter parameter) {
        return this.smartChangeLogService.listChangeLog(parameter);
    }
}
