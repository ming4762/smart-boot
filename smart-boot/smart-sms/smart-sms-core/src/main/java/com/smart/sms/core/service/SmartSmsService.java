package com.smart.sms.core.service;

import com.smart.sms.core.parameter.SmsSendParameter;
import com.smart.sms.core.result.SmsSendResult;

/**
 * 短信服务类
 * @author zhongming4762
 * 2023/5/30
 */
public interface SmartSmsService {

    /**
     * 发送短信，使用默认的通道
     * @param sendParameter 发送参数
     * @return 发送结果
     */
    SmsSendResult send(SmsSendParameter sendParameter);

    /**
     * 发送短信，指定短信通道编码
     * @param channelCode 通道编码
     * @param sendParameter 发送参数
     * @return 发送结果
     */
    SmsSendResult send(String channelCode, SmsSendParameter sendParameter);

    /**
     * 发送短信，指定通道ID
     * @param channelId 通道ID
     * @param sendParameter 发送参数
     * @return 发送结果
     */
    SmsSendResult send(Long channelId, SmsSendParameter sendParameter);

}
