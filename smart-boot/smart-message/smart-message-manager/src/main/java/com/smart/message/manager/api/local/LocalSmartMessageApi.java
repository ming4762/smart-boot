package com.smart.message.manager.api.local;

import com.message.core.parameter.SmsSendParameter;
import com.message.core.service.SmartSmsService;
import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.dto.SmsSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 本地消息接口
 * @author zhongming4762
 * 2023/6/6
 */
@Component
@Primary
public class LocalSmartMessageApi implements SmartMessageApi {

    private final SmartSmsService smartSmsService;

    public LocalSmartMessageApi(SmartSmsService smartSmsService) {
        this.smartSmsService = smartSmsService;
    }

    /**
     * 发送短信
     *
     * @param parameter 发送短信参数
     * @return 返回结果
     */
    @Override
    public SmsSendDTO sendSms(RemoteSmsSendParameter parameter) {
        SmsSendDTO sendResult;
        SmsSendParameter sendParameter = new SmsSendParameter();
        BeanUtils.copyProperties(parameter, sendParameter);
        if (parameter.getChannelId() != null) {
            sendResult = this.smartSmsService.send(parameter.getChannelId(), sendParameter);
        } else if (StringUtils.hasText(parameter.getChannelCode())) {
            sendResult = this.smartSmsService.send(parameter.getChannelCode(), sendParameter);
        } else {
            sendResult = this.smartSmsService.send(sendParameter);
        }
        return sendResult;
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
