package com.smart.sms.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.commons.core.exception.BusinessException;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.sms.manager.mapper.SmartSmsChannelManagerMapper;
import com.smart.sms.manager.model.SmartSmsChannelManagerPO;
import com.smart.sms.manager.service.SmartSmsChannelManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* smart_sms_channel_manager - 短信通道表 Service实现类
* @author SmartCodeGenerator
* 2023年5月26日 上午9:34:41
*/
@Service
@Slf4j
public class SmartSmsChannelManagerServiceImpl extends BaseServiceImpl<SmartSmsChannelManagerMapper, SmartSmsChannelManagerPO> implements SmartSmsChannelManagerService {

    /**
     * 设置默认通道
     *
     * @param id 通道ID
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefault(Long id) {
        SmartSmsChannelManagerPO manager = this.getById(id);
        if (manager == null) {
            log.warn("id不存在无法设置默认，id：{}", id);
            throw new BusinessException("不存在的ID");
        }
        if (Boolean.TRUE.equals(manager.getIsDefault())) {
            return true;
        }
        // 将现有的默认修改为false
        this.update(
                new UpdateWrapper<SmartSmsChannelManagerPO>().lambda()
                        .set(SmartSmsChannelManagerPO::getIsDefault, Boolean.FALSE)
                        .eq(SmartSmsChannelManagerPO::getIsDefault, Boolean.TRUE)
        );
        return this.update(
                new UpdateWrapper<SmartSmsChannelManagerPO>().lambda()
                        .set(SmartSmsChannelManagerPO::getIsDefault, Boolean.TRUE)
                        .eq(SmartSmsChannelManagerPO::getId, id)
        );
    }
}