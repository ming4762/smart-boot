package com.smart.message.dingding.sender;

import com.message.core.service.SmartMessageSender;
import com.smart.module.api.message.constants.MessageChannelEnum;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;

/**
 * 钉钉工作通知
 * @author shizhongming
 * 2024/4/26 17:14
 * @since 3.0.0
 */
public class SmartDingdingWorkNoticeSender implements SmartMessageSender {
    /**
     * 获取支持的通道信息
     *
     * @return 支持的消息通道
     */
    @Override
    public MessageChannelEnum supportChannel() {
        return MessageChannelEnum.DINGDING_WORK_NOTICE;
    }

    /**
     * 发送消息
     *
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    @Override
    public MessageSendDTO send(RemoteMessageSendParameter parameter) {
        return null;
    }
}
