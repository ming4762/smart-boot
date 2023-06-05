package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.module.api.system.dto.SerialCodeCreateDTO;
import com.smart.module.api.system.parameter.SerialCodeCreateParameter;
import com.smart.system.model.SmartSerialNoPO;

import java.util.List;

/**
* smart_serial_no - 业务编码表 Service
* @author SmartCodeGenerator
* 2023年6月2日 上午9:56:44
*/
public interface SmartSerialNoService extends BaseService<SmartSerialNoPO> {

    /**
     * 批量获取业务编码
     * @param parameterList 参数列表
     * @return 业务编码列表
     */
    List<SerialCodeCreateDTO> createSerial(List<SerialCodeCreateParameter> parameterList);

}