package com.message.core.service;

import com.message.core.parameter.SmsSendParameter;
import com.smart.module.api.message.constants.SmartSmsChannelEnum;
import com.smart.module.api.message.dto.SmsSendDTO;

/**
 * 发送短信通道服务类
 * @author zhongming4762
 * 2023/5/25
 */
public interface SmartSmsChannelService {

    /**
     * 获取支持的通道
     * @return SmartSmsSupportEnum
     */
    SmartSmsChannelEnum support();

    /**
     * 发送短信接口
     * @param channelProperties 通道参数
     * @param sendParameter 发送参数
     * @return 响应结果
     */
    SmsSendDTO send(String channelProperties, SmsSendParameter sendParameter);
}
