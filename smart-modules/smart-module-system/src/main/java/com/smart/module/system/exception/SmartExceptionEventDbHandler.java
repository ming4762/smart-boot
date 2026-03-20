package com.smart.module.system.exception;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.event.SmartEventHandler;
import com.smart.framework.commons.core.exception.SmartExceptionEventData;
import com.smart.framework.exception.event.SmartExceptionEvent;
import com.smart.module.system.model.SysExceptionPO;
import com.smart.module.system.service.SysExceptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * 监听异常事件，记录异常信息到数据库
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-17 22:53
 * @since 5.0.0
 */
@Component
@RequiredArgsConstructor
public class SmartExceptionEventDbHandler implements SmartEventHandler<SmartExceptionEvent> {

    private final SysExceptionService sysExceptionService;

    /**
     * 处理事件
     *
     * @param event 事件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handle(SmartExceptionEvent event) {
        SmartExceptionEventData data = event.getData();

        SysExceptionPO po = new SysExceptionPO();
        BeanUtils.copyProperties(data, po);
        po.setId(data.getExceptionNo());
        po.setCreateTime(ZonedDateTime.now());
        if (data.getOperateUserId() == null) {
            RestUserDetails currentUser = AuthUtils.getCurrentUser();
            if (currentUser != null) {
                po.setOperateUserId(currentUser.getUserId());
                po.setOperationBy(currentUser.getFullName());
                po.setTenantId(AuthUtils.getCurrentTenantId());
            }
        }
        this.sysExceptionService.save(po);
    }
}
