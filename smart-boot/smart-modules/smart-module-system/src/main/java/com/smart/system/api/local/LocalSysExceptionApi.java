package com.smart.system.api.local;

import com.smart.module.api.system.SysExceptionApi;
import com.smart.module.api.system.dto.SysExceptionSaveDTO;
import com.smart.system.model.SysExceptionPO;
import com.smart.system.service.SysExceptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author zhongming4762
 * 2023/3/12
 */
@Component
@Primary
public class LocalSysExceptionApi implements SysExceptionApi {

    private final SysExceptionService sysExceptionService;

    public LocalSysExceptionApi(SysExceptionService sysExceptionService) {
        this.sysExceptionService = sysExceptionService;
    }

    /**
     * 保存异常信息
     *
     * @param parameter 参数
     * @return 异常信息ID
     */
    @Override
    public Boolean saveException(SysExceptionSaveDTO parameter) {
        SysExceptionPO po = new SysExceptionPO();
        BeanUtils.copyProperties(parameter, po);
        return this.sysExceptionService.save(po);
    }
}
