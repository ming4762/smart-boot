package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.system.model.SysParameterPO;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
* sys_parameter - 系统参数表 Service
* @author SmartCodeGenerator
* 2023-2-27
*/
public interface SysParameterService extends BaseService<SysParameterPO> {

    /**
     * 获取参数值
     * @param code 系统参数编码
     * @return 系统参数值
     */
    @Nullable
    String getParameter(@NonNull String code);
}