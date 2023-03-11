package com.smart.module.api.system;

import com.smart.module.api.system.dto.SysLogSaveDTO;

/**
 * 日志API
 * @author zhongming4762
 * 2023/3/11
 */
public interface SysLogApi {

    /**
     * 保存日志
     * @param parameter 参数
     * @return 是否保存成功
     */
    boolean saveLog(SysLogSaveDTO parameter);
}
