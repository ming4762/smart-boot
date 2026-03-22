package com.smart.module.system.service;

import com.smart.framework.commons.core.log.SysLogSaveDTO;
import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.SysLogPO;

/**
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0.7
 */
public interface SysLogService extends BaseService<SysLogPO> {

    /**
     * 保存日志
     * @param dto 日志信息
     * @return 是否保存成功
     */
    boolean saveLog(SysLogSaveDTO dto);
}
