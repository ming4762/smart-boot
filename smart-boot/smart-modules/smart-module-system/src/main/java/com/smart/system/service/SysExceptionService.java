package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.system.model.SysExceptionPO;
import com.smart.system.pojo.dto.exception.ExceptionFeedbackDTO;
import com.smart.system.pojo.dto.exception.SysExceptionMarkResolvedParameter;

/**
* sys_exception - 系统异常信息 Service
* @author GCCodeGenerator
* 2022年6月10日
*/
public interface SysExceptionService extends BaseService<SysExceptionPO> {

    /**
     * 反馈异常信息
     * @param parameter 反馈信息
     * @return 是否反馈成功
     */
    boolean feedback(ExceptionFeedbackDTO parameter);

    /**
     * 标记异常已解决
     * @param parameter 参数
     * @return 是否操作成功
     */
    boolean markResolved(SysExceptionMarkResolvedParameter parameter);
}
