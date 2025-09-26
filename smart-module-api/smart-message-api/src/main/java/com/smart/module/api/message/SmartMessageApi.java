package com.smart.module.api.message;

import com.smart.module.api.message.dto.MessageSendResult;
import com.smart.module.api.message.dto.SmsSendResult;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;

import java.util.List;

/**
 * 消息模块API
 * @author zhongming4762
 * 2023/5/31 17:57
 */
public interface SmartMessageApi {

    /**
     * 发送短信
     * @param parameter 发送短信参数
     * @return 返回结果
     */
    SmsSendResult sendSms(RemoteSmsSendParameter parameter);

    /**
     * 发送消息
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    List<MessageSendResult> send(RemoteMessageSendParameter parameter);
}
