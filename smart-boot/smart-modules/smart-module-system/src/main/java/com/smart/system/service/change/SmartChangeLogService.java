package com.smart.system.service.change;

import com.smart.crud.service.BaseService;
import com.smart.module.api.system.dto.SmartChangeLogListDTO;
import com.smart.module.api.system.parameter.RemoteChangeLogListParameter;
import com.smart.module.api.system.parameter.RemoteChangeLogSaveParameter;
import com.smart.system.model.SmartChangeLogPO;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/7/31 18:01
 */
public interface SmartChangeLogService extends BaseService<SmartChangeLogPO> {
    /**
     * 保存修改记录
     * @param parameter 参数
     * @return 是否保存成功
     */
    boolean saveChangeLog(RemoteChangeLogSaveParameter parameter);

    /**
     * 查询修改记录
     * @param parameter 参数
     * @return 修改记录列表
     */
    List<SmartChangeLogListDTO> listChangeLog(RemoteChangeLogListParameter parameter);
}
