package com.smart.module.api.system;

import com.smart.module.api.system.dto.SerialCodeCreateDTO;
import com.smart.module.api.system.parameter.SerialCodeCreateParameter;

import java.util.List;

/**
 * 系统工具API
 * @author zhongming4762
 * 2023/6/2
 */
public interface SysToolApi {

    /**
     * 生成业务编码
     * @param parameter 参数
     * @return 业务编码
     */
    SerialCodeCreateDTO createSerial(SerialCodeCreateParameter parameter);

    /**
     * 批量生成业务编码
     * @param parameterList 参数列表
     * @return 业务编码列表
     */
    List<SerialCodeCreateDTO> createSerial(List<SerialCodeCreateParameter> parameterList);
}
