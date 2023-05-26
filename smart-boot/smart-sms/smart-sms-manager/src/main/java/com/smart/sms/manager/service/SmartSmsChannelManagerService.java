package com.smart.sms.manager.service;

import com.smart.crud.service.BaseService;
import com.smart.sms.manager.model.SmartSmsChannelManagerPO;

/**
* smart_sms_channel_manager - 短信通道表 Service
* @author SmartCodeGenerator
* 2023年5月26日 上午9:34:41
*/
public interface SmartSmsChannelManagerService extends BaseService<SmartSmsChannelManagerPO> {

    /**
     * 设置默认通道
     * @param id 通道ID
     * @return 是否修改成功
     */
    boolean setDefault(Long id);
}