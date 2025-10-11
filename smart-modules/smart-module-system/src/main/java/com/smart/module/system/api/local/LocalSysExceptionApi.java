package com.smart.module.system.api.local;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.module.api.system.SysExceptionApi;
import com.smart.module.api.system.dto.SysExceptionSaveDTO;
import com.smart.module.system.model.SysExceptionPO;
import com.smart.module.system.service.SysExceptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

/**
 * @author zhongming4762
 * 2023/3/12
 */
@Component
@Primary
@Slf4j
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
        po.setCreateTime(ZonedDateTime.now());
        if (parameter.getOperateUserId() == null) {
            RestUserDetails currentUser = AuthUtils.getCurrentUser();
            if (currentUser != null) {
                po.setOperateUserId(currentUser.getUserId());
                po.setOperationBy(currentUser.getFullName());
                po.setTenantId(AuthUtils.getCurrentTenantId());
            }
        }
        return this.sysExceptionService.save(po);
    }
}
