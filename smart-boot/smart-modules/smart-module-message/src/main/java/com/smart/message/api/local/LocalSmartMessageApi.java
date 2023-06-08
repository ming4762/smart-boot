package com.smart.message.api.local;

import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.message.dto.SmsSendDTO;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;
import com.smart.sms.core.parameter.SmsSendParameter;
import com.smart.sms.core.result.SmsSendResult;
import com.smart.sms.core.service.SmartSmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

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
        SmsSendResult sendResult;
        SmsSendParameter sendParameter = new SmsSendParameter();
        BeanUtils.copyProperties(parameter, sendParameter);
        if (parameter.getChannelId() != null) {
            sendResult = this.smartSmsService.send(parameter.getChannelId(), sendParameter);
        } else if (StringUtils.isNotBlank(parameter.getChannelCode())) {
            sendResult = this.smartSmsService.send(parameter.getChannelCode(), sendParameter);
        } else {
            sendResult = this.smartSmsService.send(sendParameter);
        }
        SmsSendDTO dto = new SmsSendDTO();
        dto.setResponseData(sendResult.getResponseData());
        dto.setChannelId(sendResult.getChannelId());
        dto.setChannelCode(sendResult.getChannelCode());
        dto.setChannelType(sendResult.getChannelType().name());
        return dto;
    }
}
