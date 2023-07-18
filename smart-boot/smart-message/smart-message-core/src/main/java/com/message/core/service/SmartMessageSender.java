package com.message.core.service;

import com.smart.module.api.message.constants.MessageChannelEnum;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;

/**
 * 消息发送接口
 * @author zhongming4762
 * 2023/7/14 18:02
 */
public interface SmartMessageSender {

    /**
     * 获取支持的通道信息
     * @return 支持的消息通道
     */
    MessageChannelEnum supportChannel();

    /**
     * 发送消息
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    MessageSendDTO send(RemoteMessageSendParameter parameter);
}
