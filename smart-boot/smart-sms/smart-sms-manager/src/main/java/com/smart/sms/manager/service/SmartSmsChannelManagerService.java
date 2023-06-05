package com.smart.sms.manager.service;

import com.smart.crud.service.BaseService;
import com.smart.sms.core.result.SmsSendResult;
import com.smart.sms.manager.model.SmartSmsChannelManagerPO;
import com.smart.sms.manager.pojo.parameter.SmartSmsSendTestParameter;

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


    /**
     * 获取默认的通道
     * @return 默认短信通道信息
     */
    SmartSmsChannelManagerPO getDefault();

    /**
     * 通过code获取短信通道
     * @param code 通道code
     * @return 短信通道机械能西
     */
    SmartSmsChannelManagerPO getByCode(String code);

    /**
     * 发送短息你测试
     * @param parameter 参数
     * @return 是否发送成功
     */
    SmsSendResult sendTest(SmartSmsSendTestParameter parameter);
}