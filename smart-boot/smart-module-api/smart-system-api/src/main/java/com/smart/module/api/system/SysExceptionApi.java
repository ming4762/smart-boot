package com.smart.module.api.system;

import com.smart.module.api.system.dto.SysExceptionSaveDTO;

/**
 * 异常API
 * 用于调用系统模块进行异常信息保存
 * @author zhongming4762
 * 2023/3/12
 */
public interface SysExceptionApi {

    /**
     * 保存异常信息
     * @param parameter 参数
     * @return 异常信息ID
     */
    Boolean saveException(SysExceptionSaveDTO parameter);
}
