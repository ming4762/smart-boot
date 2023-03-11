package com.smart.system.api.local;

import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.dto.SysLogSaveDTO;
import com.smart.system.constants.LogIdentEnum;
import com.smart.system.model.SysLogPO;
import com.smart.system.service.SysLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * local 系统日志API
 * @author zhongming4762
 * 2023/3/11
 */
@Component
@Primary
public class LocalSysLogApi implements SysLogApi {

    private final SysLogService sysLogService;

    public LocalSysLogApi(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    /**
     * 保存日志
     *
     * @param parameter 参数
     * @return 是否保存成功
     */
    @Override
    public boolean saveLog(SysLogSaveDTO parameter) {
        SysLogPO po = new SysLogPO();
        BeanUtils.copyProperties(parameter, po);
        po.setIdent(LogIdentEnum.INTERFACE_LOG);
        return this.sysLogService.save(po);
    }
}
